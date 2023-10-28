package application.crawler

import application.interfaces.IDataExtractor
import mu.KotlinLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import infrastructure.memoryGateways.RedisMemoryGateway

import components.interfaces.IContentProcessor
import modules.interfaces.ICrawlersManager
import components.interfaces.IFetcher
import components.interfaces.IURLParser
import core.dto.WebLink
import application.interfaces.IMediator
import storage.mediator.Actions.*

class CrawlerTest {
    private val host = "https://example.com"
    private val jedis = RedisMemoryGateway

    private lateinit var crawler: Crawler

    private val loggerMock = mock(KotlinLogging.logger("Crawler $host")::class.java)
    private val fetcherMock = mock(IFetcher::class.java)
    private val crawlersManager = mock(ICrawlersManager::class.java)
    private val storageMediatorMock = mock(IMediator::class.java)
    private val contentProcessor = mock(IContentProcessor::class.java)
    private val extractor = mock(IDataExtractor::class.java)
    private val urlParser = mock(IURLParser::class.java)

    @BeforeEach
    fun `set up`() {
        jedis.clear()

        crawler = Crawler(
            host,
            loggerMock,
            fetcherMock,
            crawlersManager,
            storageMediatorMock,
            urlParser,
            extractor
        )
    }

    @Test
    fun `sends kill request when frontier queue is empty`() {
        `when`(storageMediatorMock.request<Boolean>(FRONTIER_IS_QUEUE_EMPTY, host)).thenReturn(true)

        // act
        crawler.start()
        crawler.join()

        // assert
        verify(loggerMock).info("Started")
        verify(crawlersManager).removeTerminatedCrawler(crawler)
        verify(storageMediatorMock).request<Unit>(HOSTS_DELETE, host)
        verify(storageMediatorMock).request<Unit>(FRONTIER_DELETE_QUEUE, host)
        verify(loggerMock).info("Stopped")
    }

    @Test
    fun `communicates with frontier while queue is not empty`() {
//        val urlHashedPair = WebLink("$host/test")
//        val foundURL = WebLink("$host/someNewUrl")
//        val html = """<html><a href="${foundURL.url}">123</a></html>"""
//
//        `when`(storageMediatorMock.request<Boolean>(FRONTIER_IS_QUEUE_EMPTY, host)).thenReturn(false, true)
////        `when`(frontierMock.isQueueEmpty(host)).thenReturn(false, true)
//        `when`(storageMediatorMock.request<WebLink>(FRONTIER_PULL, host)).thenReturn(urlHashedPair)
////        `when`(frontierMock.pullURL(host)).thenReturn(urlHashedPair)
//        `when`(urlValidatorMock.canProcessURL(host, urlHashedPair)).thenReturn(true)
//        `when`(urlValidatorMock.canProcessURL(host, foundURL)).thenReturn(true)
//        `when`(fetcherMock.getPageHTML(urlHashedPair.url)).thenReturn(html)
//
//        // act
//        crawler.start()
//        crawler.join()
//
//        // assert
//        verify(loggerMock).info("Started")
//        verify(storageMediatorMock).request<Unit>(URLS_UPDATE, urlHashedPair.getHash())
////        verify(urlStorageMock).provideURL(urlHashedPair.getHash())
//        verify(storageMediatorMock).request<Unit>(HOSTS_PROVIDE_NEW, host)
////        verify(hostsStorageMock).provideHost(host)
//        verify(storageMediatorMock, times(2)).request<Boolean>(FRONTIER_IS_QUEUE_EMPTY, host)
//        verify(storageMediatorMock).request<Unit>(FRONTIER_UPDATE, host, foundURL.url)
////        verify(frontierMock).updateOrCreateQueue(host, foundURL.url)
//        verify(loggerMock).info("Stopped")
    }

    @Test
    fun `skips if html is null`() {
        val urlHashedPair = core.dto.WebLink("$host/test")
        val html = null

        `when`(storageMediatorMock.request<Boolean>(FRONTIER_IS_QUEUE_EMPTY, host)).thenReturn(false, true)
        `when`(storageMediatorMock.request<WebLink>(FRONTIER_PULL, host)).thenReturn(urlHashedPair)
//        `when`(urlValidatorMock.canProcessURL(host, urlHashedPair)).thenReturn(true)
        `when`(fetcherMock.getPageHTML(urlHashedPair.url)).thenReturn(html)

        // act
        crawler.start()
        crawler.join()

        // assert
        verify(loggerMock).info("Started")
        verify(storageMediatorMock).request<Unit>(URLS_UPDATE, urlHashedPair.getHash())
//        verify(contentProcessor, never()).processWebPage(any())
        verify(loggerMock).info("Stopped")
    }

    @Test
    fun `skips url from frontier if its not valid`() {
//        val urlHashedPair = WebLink("$host/test")
//        val urlHashedPairTwo = WebLink("$host/testTwo")
//        val html = """<html>...</html>"""
//
//        `when`(storageMediatorMock.request<Boolean>(FRONTIER_IS_QUEUE_EMPTY, host)).thenReturn(false, false, true)
//        `when`(storageMediatorMock.request<WebLink>(FRONTIER_PULL, host)).thenReturn(urlHashedPair, urlHashedPairTwo)
//        `when`(urlValidatorMock.canProcessURL(host, urlHashedPair)).thenReturn(true)
//        `when`(urlValidatorMock.canProcessURL(host, urlHashedPairTwo)).thenReturn(false)
//        `when`(fetcherMock.getPageHTML(urlHashedPair.url)).thenReturn(html)
//
//        // act
//        crawler.start()
//        crawler.join()
//
//        // assert
//        verify(loggerMock).info("Started")
//        verify(fetcherMock, never()).getPageHTML(urlHashedPairTwo.url)
//        verify(storageMediatorMock).request<Unit>(URLS_UPDATE, urlHashedPair.getHash())
//        verify(storageMediatorMock, times(3)).request<Boolean>(FRONTIER_IS_QUEUE_EMPTY, host)
//        verify(storageMediatorMock).request<Unit>(URLS_UPDATE, urlHashedPairTwo.getHash())
//        verify(storageMediatorMock, never()).request<Unit>(FRONTIER_UPDATE, anyString())
//        verify(loggerMock).info("Stopped")
    }

    @Test
    fun `skips url from html if its not valid`() {
        val urlHashedPair = WebLink("$host/test")
        val foundURL = WebLink("$host/someNewUrl")
        val html = """<html><a href="${foundURL.url}">123</a></html>"""


        `when`(storageMediatorMock.request<Boolean>(FRONTIER_IS_QUEUE_EMPTY, host)).thenReturn(false, true)
        `when`(storageMediatorMock.request<WebLink>(FRONTIER_PULL, host)).thenReturn(urlHashedPair, urlHashedPair)

//        `when`(urlValidatorMock.canProcessURL(host, urlHashedPair)).thenReturn(true)
//        `when`(urlValidatorMock.canProcessURL(host, foundURL)).thenReturn(false)

        `when`(fetcherMock.getPageHTML(urlHashedPair.url)).thenReturn(html)

        // act
        crawler.start()
        crawler.join()

        // assert
        verify(loggerMock).info("Started")
        verify(fetcherMock, never()).getPageHTML(foundURL.url)

        verify(storageMediatorMock).request<Unit>(URLS_UPDATE, urlHashedPair.getHash())
        verify(storageMediatorMock, never()).request<Unit>(URLS_UPDATE, foundURL.getHash())

//        verify(contentProcessor, never()).processWebPage(any())
        verify(loggerMock).info("Stopped")
    }
}