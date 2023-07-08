package frontier

import dto.FrontierQueue
import dto.URLRecord
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FrontierTest {
    private val frontier = Frontier
    private val host = "host"
    private val mockedURL = "url"
    private val mockedURL2 = "url"
    private val urlRecords = mutableListOf(URLRecord(mockedURL), URLRecord(mockedURL2))

    @BeforeEach
    fun init(){
        frontier.clear()
    }

    @Test
    fun `creates queue if host does not exist`(){
        val expectedResult = FrontierQueue(host, mutableListOf(urlRecords[0]))

        Assertions.assertEquals(null, frontier.getQueue(host))

        frontier.updateOrCreateQueue(host, mockedURL)
        val result = frontier.getQueue(host)

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `updates current queue`(){
        frontier.updateOrCreateQueue(host, mockedURL)
        frontier.updateOrCreateQueue(host, mockedURL2)

        val expectedResult = FrontierQueue(host, urlRecords,  false)
        val result = frontier.getQueue(host)
        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns queue or null`(){
        val expectedResult = FrontierQueue(host, mutableListOf(urlRecords[0]))
        frontier.updateOrCreateQueue(host, mockedURL)

        val result = frontier.getQueue(host)
        val emptyResult = frontier.getQueue("something")
        Assertions.assertEquals(expectedResult, result)
        Assertions.assertEquals(null, emptyResult)
    }

    @Test
    fun `allows to pull urlRecord with reformatted data from queue by host`(){
        frontier.updateOrCreateQueue(host, mockedURL)
        val result = frontier.pullURLRecord(host)
        Assertions.assertEquals(urlRecords[0], result)
        Assertions.assertNotEquals(urlRecords[0].url, mockedURL)
    }

    @Test
    fun `picks free queue and returns hostname`(){
        frontier.updateOrCreateQueue(host, mockedURL)
        val result = frontier.pickFreeQueue()
        Assertions.assertEquals(host, result)

        val expectedResult = FrontierQueue(host, mutableListOf(urlRecords[0]), true)
        Assertions.assertEquals(frontier.getQueue(host), expectedResult)
    }

    fun `deletes queue if there is no more urls`(){

    }

}