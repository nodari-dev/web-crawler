package infrastructure.gateways.memoryGateways

import infrastructure.repository.FrontierRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import redis.clients.jedis.JedisPool
import java.util.concurrent.locks.ReentrantLock
import kotlin.test.assertEquals

class FrontierRepositoryTest {
    private val jedisMock = mock(JedisPool("localhost", 6379).resource::class.java)
    private val mutexMock = mock(ReentrantLock::class.java)
    private val frontierRepository = FrontierRepository(mutexMock, jedisMock)
    private val table = "frontier"

    @AfterEach
    fun cleanup(){
        frontierRepository.clear()
    }

    @Test
    fun `create works correct`(){
        val host = "host"
        val list = listOf("url1", "url2")
        frontierRepository.create(host ,list)

        verify(mutexMock).lock()
        verify(jedisMock).rpush(host, list[0])
        verify(jedisMock).rpush(host, list[1])
        verify(jedisMock).hset(table, "$host-urls", host)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `update works correct`(){
        val host = "host"
        val list = listOf("url1", "url2")
        frontierRepository.update(host ,list)

        verify(mutexMock).lock()
        verify(jedisMock).rpush(host, list[0])
        verify(jedisMock).rpush(host, list[1])
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `assignCrawler work correct`(){
        val host = "host"
        val crawlerId = "crawler-id"

        frontierRepository.assignCrawler(host, crawlerId)
        verify(mutexMock).lock()
        verify(jedisMock).rpush("host-crawlers", crawlerId)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `unassignCrawler work correct`(){
        val host = "host"
        val crawlerId = "crawler-id"

        frontierRepository.unassignCrawler(host, crawlerId)
        verify(mutexMock).lock()
        verify(jedisMock).lrem("host-crawlers", 0, crawlerId)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `getQueuesInfo works correct`(){
        val expectedResult = mutableMapOf(
            "host-urls" to "host",
            "host-crawlers" to "host-crawlers"
        )
        `when`(jedisMock.hgetAll(table)).thenReturn(expectedResult)
        val result = frontierRepository.getQueuesInfo()
        assertEquals(expectedResult, result)

        verify(mutexMock).lock()
        verify(jedisMock).hgetAll(table)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `isQueueDefined works correct`(){
        val mockedTable = mutableMapOf(
            "host-urls" to "host",
        )
        val host = "host"
        `when`(jedisMock.hgetAll(table)).thenReturn(mockedTable)
        val result = frontierRepository.isQueueDefined(host)
        assertEquals(true, result)
        verify(mutexMock).lock()
        verify(jedisMock).hgetAll(table)
        verify(jedisMock).close()
        verify(mutexMock).unlock()

        val resultWithNotDefinedQueue = frontierRepository.isQueueDefined("something-else")
        assertEquals(false, resultWithNotDefinedQueue)
    }


    @Test
    fun `getLastItem works correct with not empty queue`(){
        val host = "host"
        val url = "url"
        `when`(jedisMock.lpop(host)).thenReturn(url)

        val result = frontierRepository.getLastItem(host)
        assertEquals(url, result)

        verify(mutexMock).lock()
        verify(jedisMock).lpop(host)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `getLastItem works correct with empty queue`(){
        val host = "not_exists"
        val notExisting = frontierRepository.getLastItem(host)
        assertEquals(null, notExisting)

        verify(mutexMock).lock()
        verify(jedisMock).lpop(host)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `delete works correct`(){
        val host = "host"
        frontierRepository.delete(host)

        verify(mutexMock).lock()
        verify(jedisMock).del(host)
        verify(jedisMock).hdel(table, "$host-urls")
        verify(jedisMock).hdel(table, "$host-crawlers")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }
}