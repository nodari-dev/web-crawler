package frontier

import dto.HashedUrlPair
import org.junit.jupiter.api.Test

class FrontierTest {
    private val frontier = Frontier
    private val host = "host"
    private val mockedURL = HashedUrlPair("url")
    private val mockedURL2 = HashedUrlPair("url2")
//    private val frontierRecords = mutableListOf(FrontierRecord(mockedURL), FrontierRecord(mockedURL2))
    
    @Test
    fun `creates queue if host does not exist`(){
//        val expectedResult = FrontierQueue(host, mutableListOf(frontierRecords[0]))
//
//        Assertions.assertEquals(null, frontier.getQueue(host))
//
//        frontier.updateOrCreateQueue(host, mockedURL)
//        val result = frontier.getQueue(host)
//
//        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `updates current queue`(){
//        frontier.updateOrCreateQueue(host, mockedURL)
//        frontier.updateOrCreateQueue(host, mockedURL2)
//
//        val expectedResult = FrontierQueue(host, frontierRecords,  false)
//        val result = frontier.getQueue(host)
//        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns queue or null`(){
//        val expectedResult = FrontierQueue(host, mutableListOf(frontierRecords[0]))
//        frontier.updateOrCreateQueue(host, mockedURL)
//
//        val result = frontier.getQueue(host)
//        val emptyResult = frontier.getQueue("something")
//        Assertions.assertEquals(expectedResult, result)
//        Assertions.assertEquals(null, emptyResult)
    }

    @Test
    fun `allows to pull urlRecord with reformatted data from queue by host`(){
        frontier.updateOrCreateQueue(host, mockedURL)
//        val result = frontier.pullURLRecord(host)
//        Assertions.assertEquals(frontierRecords[0], result)
    }

    @Test
    fun `picks free queue and returns hostname`(){
//        frontier.updateOrCreateQueue(host, mockedURL)
//        val result = frontier.pickFreeQueue()
//        Assertions.assertEquals(host, result)
//
//        val expectedResult = FrontierQueue(host, mutableListOf(frontierRecords[0]), true)
//        Assertions.assertEquals(expectedResult, frontier.getQueue(host))
    }

    @Test
    fun `blocks queue for other threads if it was picked` (){
//        frontier.updateOrCreateQueue(host, mockedURL)
//        val result = frontier.pickFreeQueue()
//        val result2 = frontier.pickFreeQueue()
//
//        Assertions.assertEquals(host, result)
//        Assertions.assertEquals(null, result2)
    }

    @Test
    fun `deletes queue if there is no more urls`(){
//        frontier.updateOrCreateQueue(host, mockedURL)
//        val host = frontier.pickFreeQueue()!!
//        frontier.pullURLRecord(host)
//
//        Assertions.assertEquals(null, frontier.pullURLRecord(host))
//        Assertions.assertEquals(null, frontier.getQueue(host))
    }

}