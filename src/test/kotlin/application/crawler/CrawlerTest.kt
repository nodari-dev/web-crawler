package application.crawler

import application.htmlAnalyzer.SEOAnalyzer
import application.interfaces.*
import application.parser.robotsparser.RobotsParser
import application.parser.urlparser.URLParser
import configuration.Configuration.CRAWLING_DELAY
import core.dto.RobotsData
import core.dto.SEO
import core.dto.URLInfo
import infrastructure.repository.interfaces.ISEORepository
import mu.KotlinLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import storage.interfaces.IFrontier
import storage.interfaces.IRobotsStorage
import storage.interfaces.IVisitedURLs
import kotlin.test.assertEquals

class CrawlerTest {
    private val frontier = mock(IFrontier::class.java)
    private val visitedURLs = mock(IVisitedURLs::class.java)
    private val robotsStorage = mock(IRobotsStorage::class.java)
    private val fetcher = mock(IFetcher::class.java)
    private val urlParser = URLParser()
    private val robotsParser = RobotsParser()
    private val urlPacker = URLPacker()
    private val seoAnalyzer = SEOAnalyzer()
    private val seoRepository = mock(ISEORepository::class.java)
    private val logger = mock(KotlinLogging.logger("Crawler")::class.java)

    private val mocks = Mocks()

    private fun createCrawler(): Crawler{
        return Crawler(
            frontier,
            visitedURLs,
            robotsStorage,
            fetcher,
            urlParser,
            robotsParser,
            urlPacker,
            seoAnalyzer,
            seoRepository,
            logger
        )
    }

    @Test
    fun `returns status`(){
        val crawler = createCrawler()
        val isCrawling = crawler.isCrawling()
        assertEquals(false, isCrawling)
        crawler.interrupt()
        crawler.join()
    }

    @Test
    fun `inits crawler`(){
        val host = "host"

        val crawler = createCrawler()

        // act
        crawler.id(0).host(host)
        crawler.start()

        Thread.sleep(1000)

        verify(frontier).assign(0, host)
        crawler.interrupt()
        crawler.join()
    }

    @Test
    fun `processes empty robots with no robots from fetcher`(){
        val host = "host.com"
        `when`(robotsStorage.get(host)).thenReturn(null)
        `when`(fetcher.downloadHTML("http://host.com/robots.txt")).thenReturn(null)

        val crawler = createCrawler()
        crawler.id(0).host(host)
        crawler.start()

        Thread.sleep(1000)

        verify(robotsStorage).update(host, RobotsData(emptyList()))
        crawler.interrupt()
        crawler.join()
    }

    @Test
    fun `processes with robots txt from fetcher`(){
        val host = "host.com"
        `when`(robotsStorage.get(host)).thenReturn(null)
        `when`(fetcher.downloadHTML("http://host.com/robots.txt")).thenReturn("Disallow: /test/ \n Crawl-delay: 10")

        val crawler = createCrawler()
        crawler.id(0).host(host)
        crawler.start()

        Thread.sleep(1000)

        verify(robotsStorage).update(host, RobotsData(listOf(URLInfo("/test")), 10))
        crawler.interrupt()
        crawler.join()
    }

    @Test
    fun `crawl stops if no url was returned from frontier`(){
        val host = "host"
        `when`(frontier.pullFrom(host)).thenReturn(null)

        val crawler = createCrawler()
        crawler.id(0)
        crawler.host(host)
        crawler.start()

        Thread.sleep(1000)

        verify(frontier).assign(0, host)
        verify(frontier).unassign(0, host)
        crawler.interrupt()
        crawler.join()
    }

    @Test
    fun `crawl works correct`(){
        val host = "host.com"

        val urls = listOf(
            URLInfo("https://www.example.com"),
            URLInfo("https://www.google.com"),
            URLInfo("https://www.github.com")
        )

        val newURLs = listOf(
            URLInfo("https://www.google.com"),
            URLInfo("https://www.github.com")
        )

        val seo = SEO(
            "Basic HTML with Links",
            null,
            "some-url/",
            "a,some,this,is,my,with,to,template,website!,links:,links,welcome,html,basic"
        )

        val urlFromFrontier = URLInfo("some-url")
        `when`(frontier.pullFrom(host)).thenReturn(urlFromFrontier, null)
        `when`(fetcher.downloadHTML(urlFromFrontier.link)).thenReturn(mocks.html)
        `when`(visitedURLs.isValid(urlFromFrontier.hash)).thenReturn(true)
        `when`(visitedURLs.filterByNewOnly(urls)).thenReturn(newURLs)

        val crawler = createCrawler()
        crawler.id(0)
        crawler.host(host)
        crawler.start()

        Thread.sleep(1000)

        verify(visitedURLs).update(urlFromFrontier)
        verify(seoRepository).put(seo)
        verify(frontier).update("www.google.com", listOf(URLInfo("https://www.google.com")))
        verify(frontier).update("www.github.com", listOf(URLInfo("https://www.github.com")))

        Thread.sleep(CRAWLING_DELAY)

        verify(frontier).unassign(0, host)
        crawler.interrupt()
        crawler.join()
    }
}