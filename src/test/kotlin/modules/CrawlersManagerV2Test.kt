package modules

import application.crawler.CrawlerV2
import application.crawler.entities.CrawlerConfig
import application.interfaces.ICrawlerFactory
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import storage.interfaces.IFrontierV2
import kotlin.test.assertEquals

class CrawlersManagerV2Test {
    private val crawlerFactoryMock = mock(ICrawlerFactory::class.java)
    private val crawlersManagerV2 = CrawlersManagerV2(crawlerFactoryMock)
    private val frontierMock = mock(IFrontierV2::class.java)
//    private val crawlerMock = mock(CrawlerV2(CrawlerConfig(0, "host"), frontierMock)::class.java)

    @Test
    fun `allows to request crawler initialization`(){
        val host = "host"
//        `when`(crawlerFactoryMock.generateCrawler(CrawlerConfig(0, host))).thenReturn(crawlerMock)

        // act
        val result = crawlersManagerV2.requestCrawlerInitializationAndGetId(host)
        val crawlersNumber = crawlersManagerV2.requestAllCrawlers()

        assertEquals(0, result)
        assertEquals(1, crawlersNumber)
    }

    @Test
    fun `allows to request crawler termination`(){
        val id = 0
        val host = "host"

        crawlersManagerV2.requestCrawlerInitializationAndGetId(host)
        val crawlersNumber = crawlersManagerV2.requestAllCrawlers()
        assertEquals(1, crawlersNumber)

        crawlersManagerV2.requestCrawlerTermination(id)

        val crawlersNumberAfterDelete = crawlersManagerV2.requestAllCrawlers()
        assertEquals(0, crawlersNumberAfterDelete)
    }

    @Test
    fun `allows to reassign crawler to another queue`(){

    }
}