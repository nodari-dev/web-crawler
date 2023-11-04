package application.crawler

import application.crawler.entities.CrawlerConfig
import application.crawler.entities.CrawlerStatus
import application.interfaces.IFetcher
import application.interfaces.IURLParser
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import storage.interfaces.IFrontierV2

class CrawlerV2Test {
    private val config = CrawlerConfig(0, "host")
    private val frontierMock = mock(IFrontierV2::class.java)
    private val fetcherMock = mock(IFetcher::class.java)
    private val urlParserMock = mock(IURLParser::class.java)
    private val crawler = CrawlerV2(config, frontierMock, fetcherMock, urlParserMock)

    @Test
    fun `returns status`(){
        val alive = crawler.getStatus().isAlive
        val isWorking = crawler.getStatus().isWorking
        assertEquals(false, alive)
        assertEquals(false, isWorking)
    }

    @Test
    fun `returns config`(){
        val id = 0
        val host = "host"
        assertEquals(id, crawler.getConfig().id)
        assertEquals(host, crawler.getConfig().host)
    }

    @Test
    fun `allows to reassign`(){
        val host = "hostNew"

        // act
        crawler.reassign(host)

        assertEquals(host, crawler.getConfig().host)
    }

    @Test
    fun `allows to be terminated`(){
        // act
        crawler.run()
        assertEquals(CrawlerStatus(isAlive = false, isWorking = true),crawler.getStatus())

        crawler.terminate()
        assertEquals(CrawlerStatus(isAlive = false, isWorking = false),crawler.getStatus())
    }
}