package application

import application.crawler.entities.CrawlerConfig
import application.interfaces.IFetcher
import application.interfaces.IURLParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import storage.interfaces.IFrontierV2

class CrawlerFactoryTest {
    private val frontierMock = mock(IFrontierV2::class.java)
    private val fetcherMock = mock(IFetcher::class.java)
    private val urlParserMock = mock(IURLParser::class.java)
    private val crawlerFactory = CrawlerFactory(frontierMock, fetcherMock, urlParserMock)

    @Test
    fun `creates new crawler`(){
        val config = CrawlerConfig(0, "host")
        val crawler = crawlerFactory.generateCrawler(config)

        assertEquals(config ,crawler.getConfig())
    }
}