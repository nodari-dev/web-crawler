package application.crawler

import application.crawler.entities.CrawlerSettings
import application.interfaces.IFetcher
import application.interfaces.IURLPacker
import application.interfaces.IURLParser
import mu.KotlinLogging
import org.mockito.Mockito
import org.mockito.Mockito.mock
import storage.interfaces.IFrontier
import storage.interfaces.IVisitedURLs

class CrawlerV2Test {
    private val config = CrawlerSettings(0, "host")
    private val frontierMock = mock(IFrontier::class.java)
    private val visitedURLsMock = mock(IVisitedURLs::class.java)
    private val fetcherMock = mock(IFetcher::class.java)
    private val urlParserMock = mock(IURLParser::class.java)
    private val urlPacker = mock(IURLPacker::class.java)
    private val mockLogger = Mockito.mock(KotlinLogging.logger("Crawler")::class.java)
    private val id = 0
    private val crawler = CrawlerV2(frontierMock, visitedURLsMock, fetcherMock, urlParserMock, urlPacker, mockLogger).id(id)
//
//    @Test
//    fun `returns status`(){
//        val alive = crawler.getStatus().isAlive
//        val isWorking = crawler.getStatus().isWorking
//        assertEquals(false, alive)
//        assertEquals(false, isWorking)
//    }
//
//    @Test
//    fun `returns config`(){
//        val id = 0
//        val host = "host"
//        assertEquals(id, crawler.getConfig().id)
//        assertEquals(host, crawler.getConfig().host)
//    }
//
//    @Test
//    fun `allows to reassign`(){
//        val host = "hostNew"
//
//        // act
//        crawler.reassign(host)
//
//        assertEquals(host, crawler.getConfig().host)
//    }
//
//    @Test
//    fun `allows to be terminated`(){
//        // act
//        crawler.run()
//        assertEquals(CrawlerStatus(isAlive = false, isWorking = true),crawler.getStatus())
//
//        crawler.terminate()
//        assertEquals(CrawlerStatus(isAlive = false, isWorking = false),crawler.getStatus())
//    }
}