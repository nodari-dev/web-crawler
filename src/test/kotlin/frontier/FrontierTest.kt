package frontier

import dto.FrontierQueue
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FrontierTest {
    private val frontier = Frontier
    private val host = "host"
    private val newHost = "newHost"
    private val urls = mutableListOf("1", "2")
    private val newUrls = mutableListOf("3", "4")

    @BeforeEach
    fun init(){
        frontier.clear()
    }


    @Test
    fun `creates queue if host does not exist`(){
        val expectedResult = FrontierQueue(host, urls)

        Assertions.assertEquals(null, frontier.getQueue(host))

        frontier.updateOrCreateQueue(host, urls)
        val result = frontier.getQueue(host)

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `updates current queue`(){
        frontier.updateOrCreateQueue(host, urls)
        frontier.updateOrCreateQueue(host, newUrls)

        val expectedResult = FrontierQueue(host, mutableListOf("1", "2", "3", "4"), true)
        val result = frontier.getQueue(host)
        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns queue or null`(){
        val urls = mutableListOf("url", "url2")
        val expectedResult = FrontierQueue(host, urls)
        frontier.updateOrCreateQueue(host, urls)

        val result = frontier.getQueue(host)
        val emptyResult = frontier.getQueue("something")
        Assertions.assertEquals(expectedResult, result)
        Assertions.assertEquals(null, emptyResult)
    }

    @Test
    fun `pulls url from queue by host`(){
        frontier.updateOrCreateQueue(host, urls)
        val result = frontier.pullURL(host)
        Assertions.assertEquals("1", result)
    }

    @Test
    fun `returns free queue and blocks it`(){
        frontier.updateOrCreateQueue(host, urls)
        val result = frontier.pickFreeQueue()
        Assertions.assertEquals(host, result)

        val expectedResult = FrontierQueue(host, urls, true)
        Assertions.assertEquals(frontier.getQueue(host), expectedResult)
    }

    fun `deletes queue if there is no more urls`(){

    }

}