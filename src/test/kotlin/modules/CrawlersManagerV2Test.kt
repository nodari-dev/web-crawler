package modules

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import storage.interfaces.IFrontier
import kotlin.test.assertEquals

class CrawlersManagerV2Test {

    private val frontierMock = mock(IFrontier::class.java)
    private val crawlersManagerV2 = CrawlersManagerV2(frontierMock)

    @Test
    fun `allows to request crawler initialization`(){
        val host = "host"
        val hostTwo = "host2"

        // act
        val result = crawlersManagerV2.requestCrawlerInitialization(host)
        val crawlersNumber = crawlersManagerV2.requestAllCrawlers()

        assertEquals(0, result)
        assertEquals(1, crawlersNumber)

        val resultSecondCrawler = crawlersManagerV2.requestCrawlerInitialization(hostTwo)
        val crawlersNumberAfterSecond = crawlersManagerV2.requestAllCrawlers()

        assertEquals(2, resultSecondCrawler)
        assertEquals(2, crawlersNumberAfterSecond)
    }

    @Test
    fun `allows to request crawler termination`(){
        val id = 0
        val host = "host"

        crawlersManagerV2.requestCrawlerInitialization(host)
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