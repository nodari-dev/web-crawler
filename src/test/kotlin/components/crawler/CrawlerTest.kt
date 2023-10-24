package components.crawler

import core.configuration.Configuration.SAVE_FILE_LOCATION
import mu.KotlinLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import adatapters.gateways.memoryGateways.RedisMemoryGateway
import components.dataExtractor.DataExtractor
import components.fetcher.Fetcher
import components.parser.urlparser.URLParser
import components.storage.frontier.Frontier
import components.storage.hosts.HostsStorage
import components.storage.url.URLStorage

class CrawlerTest {
    private val host = "https://example.com"
    private val jedis = RedisMemoryGateway

    private lateinit var crawler: Crawler

    private val loggerMock = mock(KotlinLogging.logger("Crawler $host")::class.java)
    private val fetcherMock = mock(Fetcher::class.java)
    private val urlValidatorMock = mock(URLValidator::class.java)
    private val dataExtractorMock = mock(DataExtractor::class.java)
    private val crawlersFactoryMock = mock(CrawlersManager::class.java)
    private val hostsStorageMock = mock(HostsStorage::class.java)
    private val urlStorageMock = mock(URLStorage::class.java)
    private val frontierMock = mock(Frontier::class.java)

    @BeforeEach
    fun `set up`() {
        jedis.clear()

        crawler = Crawler(
            host,
            loggerMock,
            fetcherMock,
            urlValidatorMock,
            URLParser(),
            crawlersFactoryMock,
            dataExtractorMock,
            hostsStorageMock,
            urlStorageMock,
            frontierMock
        )
    }

    @Test
    fun `sends kill request when frontier queue is empty`() {
        `when`(frontierMock.isQueueEmpty(host)).thenReturn(true)

        // act
        crawler.start()
        crawler.join()

        // assert
        verify(loggerMock).info("Started")
        verify(crawlersFactoryMock).removeTerminatedCrawler(crawler)
        verify(hostsStorageMock).deleteHost(host)
        verify(frontierMock).deleteQueue(host)
        verify(loggerMock).info("Stopped")
    }

    @Test
    fun `communicates with frontier while queue is not empty`() {
        val urlHashedPair = core.dto.WebLink("$host/test")
        val foundURL = core.dto.WebLink("$host/someNewUrl")
        val html = """<html><a href="${foundURL.url}">123</a></html>"""

        `when`(frontierMock.isQueueEmpty(host)).thenReturn(false, true)
        `when`(frontierMock.pullURL(host)).thenReturn(urlHashedPair)
        `when`(urlValidatorMock.canProcessURL(host, urlHashedPair)).thenReturn(true)
        `when`(urlValidatorMock.canProcessURL(host, foundURL)).thenReturn(true)
        `when`(fetcherMock.getPageHTML(urlHashedPair.url)).thenReturn(html)

        // act
        crawler.start()
        crawler.join()

        // assert
        verify(loggerMock).info("Started")
        verify(dataExtractorMock).extractSEODataToFile(html, urlHashedPair.url, SAVE_FILE_LOCATION)
        verify(urlStorageMock).provideURL(urlHashedPair.getHash())
        verify(hostsStorageMock).provideHost(host)
        verify(frontierMock).updateOrCreateQueue(host, foundURL.url)
        verify(loggerMock).info("Stopped")
    }

    @Test
    fun `skips if html is null`() {
        val urlHashedPair = core.dto.WebLink("$host/test")
        val html = null

        `when`(frontierMock.isQueueEmpty(host)).thenReturn(false, true)
        `when`(frontierMock.pullURL(host)).thenReturn(urlHashedPair)
        `when`(urlValidatorMock.canProcessURL(host, urlHashedPair)).thenReturn(true)
        `when`(fetcherMock.getPageHTML(urlHashedPair.url)).thenReturn(html)

        // act
        crawler.start()
        crawler.join()

        // assert
        verify(loggerMock).info("Started")
        verify(dataExtractorMock, never()).extractSEODataToFile(anyString(), anyString(), anyString())
        verify(urlStorageMock).provideURL(urlHashedPair.getHash())
        verify(frontierMock, never()).updateOrCreateQueue(anyString(), anyString())
        verify(loggerMock).info("Stopped")
    }

    @Test
    fun `skips url from frontier if its not valid`() {
        val urlHashedPair = core.dto.WebLink("$host/test")
        val urlHashedPairTwo = core.dto.WebLink("$host/testTwo")
        val html = """<html>...</html>"""

        `when`(frontierMock.isQueueEmpty(host)).thenReturn(false, false, true)
        `when`(frontierMock.pullURL(host)).thenReturn(urlHashedPair, urlHashedPairTwo)
        `when`(urlValidatorMock.canProcessURL(host, urlHashedPair)).thenReturn(true)
        `when`(urlValidatorMock.canProcessURL(host, urlHashedPairTwo)).thenReturn(false)
        `when`(fetcherMock.getPageHTML(urlHashedPair.url)).thenReturn(html)

        // act
        crawler.start()
        crawler.join()

        // assert
        verify(loggerMock).info("Started")
        verify(dataExtractorMock).extractSEODataToFile(html, urlHashedPair.url, SAVE_FILE_LOCATION)
        verify(fetcherMock, never()).getPageHTML(urlHashedPairTwo.url)
        verify(urlStorageMock).provideURL(urlHashedPair.getHash())
        verify(urlStorageMock, never()).provideURL(urlHashedPairTwo.getHash())
        verify(frontierMock, never()).updateOrCreateQueue(anyString(), anyString())
        verify(loggerMock).info("Stopped")
    }

    @Test
    fun `skips url from html if its not valid`() {
        val urlHashedPair = core.dto.WebLink("$host/test")
        val foundURL = core.dto.WebLink("$host/someNewUrl")
        val html = """<html><a href="${foundURL.url}">123</a></html>"""


        `when`(frontierMock.isQueueEmpty(host)).thenReturn(false, true)
        `when`(frontierMock.pullURL(host)).thenReturn(urlHashedPair, urlHashedPair)

        `when`(urlValidatorMock.canProcessURL(host, urlHashedPair)).thenReturn(true)
        `when`(urlValidatorMock.canProcessURL(host, foundURL)).thenReturn(false)

        `when`(fetcherMock.getPageHTML(urlHashedPair.url)).thenReturn(html)

        // act
        crawler.start()
        crawler.join()

        // assert
        verify(loggerMock).info("Started")
        verify(dataExtractorMock).extractSEODataToFile(html, urlHashedPair.url, SAVE_FILE_LOCATION)
        verify(fetcherMock, never()).getPageHTML(foundURL.url)

        verify(urlStorageMock).provideURL(urlHashedPair.getHash())
        verify(urlStorageMock, never()).provideURL(foundURL.getHash())

        verify(frontierMock, never()).updateOrCreateQueue(host, urlHashedPair.url)
        verify(loggerMock).info("Stopped")
    }
}