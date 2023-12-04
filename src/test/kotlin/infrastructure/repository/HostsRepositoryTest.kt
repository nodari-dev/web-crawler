package infrastructure.repository

import core.dto.URLInfo
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import redis.clients.jedis.JedisPool
import java.util.concurrent.locks.ReentrantLock
import kotlin.test.assertEquals

class HostsRepositoryTest{
    private val jedisMock = Mockito.mock(JedisPool("localhost", 6379).resource::class.java)
    private val mutexMock = Mockito.mock(ReentrantLock::class.java)
    private val hostsRepository = RobotsRepository(mutexMock, jedisMock)

    @Test
    fun `update works correct`(){
        val host = "host"
        val urlsInfo = listOf(URLInfo("/some-blocked-url"), URLInfo("/another-blocked-url"))
        hostsRepository.update(host, urlsInfo)

        verify(mutexMock).lock()
        verify(jedisMock).rpush("hosts-info:$host", urlsInfo[0].link)
        verify(jedisMock).rpush("hosts-info:$host", urlsInfo[1].link)
        verify(mutexMock).unlock()
    }

    @Test
    fun `get works correct`(){
        val host = "host"
        val blockedURLs = listOf(URLInfo("/some-blocked-url"), URLInfo("/another-blocked-url"))
        val expectedResult = mutableListOf(blockedURLs[0].link, blockedURLs[1].link )
        `when`(jedisMock.lrange("hosts-info:$host", 0, -1))
            .thenReturn(expectedResult)

        val result = hostsRepository.get(host)

        verify(mutexMock).lock()
        assertEquals(expectedResult, result)
        verify(mutexMock).unlock()
    }
}