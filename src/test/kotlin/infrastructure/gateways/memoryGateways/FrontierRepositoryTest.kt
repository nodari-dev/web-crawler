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
    private val redisRepository = FrontierRepository(mutexMock, jedisMock)
    private val table = "frontier"
    private val field = "queues"


    @AfterEach
    fun cleanup(){
        redisRepository.clear()
    }

    @Test
    fun `create works correct`(){
        val host = "host"
        val list = listOf("url1", "url2")
        redisRepository.create(host ,list)

        verify(mutexMock).lock()
        verify(jedisMock).rpush(host, list[0])
        verify(jedisMock).rpush(host, list[1])
        verify(jedisMock).hset(table, "$host-urls", host)
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
        val result = redisRepository.getQueuesInfo()
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
        val result = redisRepository.isQueueDefined(host)
        assertEquals(true, result)
        verify(mutexMock).lock()
        verify(jedisMock).hgetAll(table)
        verify(jedisMock).close()
        verify(mutexMock).unlock()

        val resultWithNotDefinedQueue = redisRepository.isQueueDefined("something-else")
        assertEquals(false, resultWithNotDefinedQueue)
    }


    @Test
    fun `getLastItem works correct with not empty queue`(){
        val host = "host"
        val url = "url"
        `when`(jedisMock.lpop(host)).thenReturn(url)

        val result = redisRepository.getLastItem(host)
        assertEquals(url, result)

        verify(mutexMock).lock()
        verify(jedisMock).lpop(host)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `getLastItem works correct with empty queue`(){
        val host = "not_exists"
        val notExisting = redisRepository.getLastItem(host)
        assertEquals(null, notExisting)

        verify(mutexMock).lock()
        verify(jedisMock).lpop(host)
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `delete works correct`(){
        val host = "host"
        redisRepository.delete(host)

        verify(mutexMock).lock()
        verify(jedisMock).del(host)
        verify(jedisMock).hdel(table, "$host-urls")
        verify(jedisMock).hdel(table, "$host-crawlers")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }
}