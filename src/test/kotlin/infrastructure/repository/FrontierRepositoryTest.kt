package infrastructure.repository

import java.util.concurrent.locks.ReentrantLock
import kotlin.test.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import redis.clients.jedis.JedisPool

import core.dto.URLInfo

class FrontierRepositoryTest {
    private val jedisMock = mock(JedisPool("localhost", 6379).resource::class.java)
    private val mutexMock = mock(ReentrantLock::class.java)
    private val frontierRepository = FrontierRepository(mutexMock, jedisMock)

    @AfterEach
    fun cleanup(){
        frontierRepository.clear()
    }

    @Test
    fun `get works correct with not empty queue`(){
        val host = "host"
        val url = URLInfo("url")
        `when`(jedisMock.lpop("frontier:$host:urls")).thenReturn("url")

        val result = frontierRepository.get(host)
        assertEquals(url, result)

        verify(mutexMock).lock()
        verify(jedisMock).lpop("frontier:$host:urls")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `get works correct with empty queue`(){
        val host = "not_exists"
        val notExisting = frontierRepository.get(host)
        assertEquals(null, notExisting)

        verify(mutexMock).lock()
        verify(jedisMock).lpop("frontier:$host:urls")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `update works correct`(){
        val host = "host"
        val list = listOf(URLInfo("url1"), URLInfo("url2"))
        frontierRepository.update(host ,list)

        verify(mutexMock).lock()
        verify(jedisMock).rpush("frontier:$host:urls", list[0].link)
        verify(jedisMock).rpush("frontier:$host:urls", list[1].link)
        verify(jedisMock).set("frontier:$host:available", "no")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `assignCrawler works correct`(){
        val host = "host"
        val crawlerId = 1

        frontierRepository.assignCrawler(crawlerId, host)
        verify(mutexMock).lock()
        verify(jedisMock).rpush("frontier:$host:crawlerIds", crawlerId.toString())
        verify(jedisMock).set("frontier:$host:available", "no")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `unassignCrawler works correct`(){
        val host = "host"
        val crawlerId = 1

        frontierRepository.unassignCrawler(crawlerId, host)
        verify(mutexMock).lock()
        verify(jedisMock).lrem("frontier:$host:crawlerIds", 0, crawlerId.toString())
        verify(jedisMock).set("frontier:$host:available", "no")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `unassignAllCrawler works correct`(){
        `when`(jedisMock.keys("frontier:*:crawlerIds"))
            .thenReturn(mutableSetOf("frontier:host:crawlerIds", "frontier:host2:crawlerIds"))

        frontierRepository.unassignAllCrawlers()
        verify(mutexMock).lock()
        verify(jedisMock).del("frontier:host:crawlerIds")
        verify(jedisMock).del("frontier:host2:crawlerIds")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `getAvailableQueue works correct`(){
        val availableHost = "frontier:host.com:available"
        val unavailableHost = "frontier:anotherhost.com:available"
        `when`(jedisMock.keys("frontier:*:available")).thenReturn(
            setOf(availableHost, unavailableHost)
        )

        `when`(jedisMock.get(unavailableHost)).thenReturn("no")
        `when`(jedisMock.get(availableHost)).thenReturn("yes")

        val expectedResult = "host.com"
        val result = frontierRepository.getAvailableQueue()
        assertEquals(expectedResult, result)
        verify(mutexMock).lock()
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `changes queue availability to yes`() {
        val host = "host"
        `when`(jedisMock.lrange("frontier:$host:crawlerIds", 0, -1)).thenReturn(mutableListOf("1"))
        `when`(jedisMock.lrange("frontier:$host:urls", 0, -1)).thenReturn(mutableListOf("url1", "url2"))
        val list = listOf(URLInfo("url1"))
        frontierRepository.update(host ,list)

        verify(mutexMock).lock()
        verify(jedisMock).rpush("frontier:$host:urls", list[0].link)
        verify(jedisMock).set("frontier:$host:available", "yes")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }

    @Test
    fun `changes queue availability to no`() {
        val host = "host"
        `when`(jedisMock.lrange("frontier:$host:crawlerIds", 0, -1)).thenReturn(mutableListOf("1", "2"))
        `when`(jedisMock.lrange("frontier:$host:urls", 0, -1)).thenReturn(mutableListOf("url1"))
        val list = listOf(URLInfo("url1"))
        frontierRepository.update(host ,list)

        verify(mutexMock).lock()
        verify(jedisMock).rpush("frontier:$host:urls", list[0].link)
        verify(jedisMock).set("frontier:$host:available", "no")
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }
}