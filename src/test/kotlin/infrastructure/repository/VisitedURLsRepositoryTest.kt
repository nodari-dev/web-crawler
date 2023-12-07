package infrastructure.repository

import java.util.concurrent.locks.ReentrantLock
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import redis.clients.jedis.JedisPool

import core.dto.URLInfo

class VisitedURLsRepositoryTest {
    private val jedisMock = mock(JedisPool("localhost", 6379).resource::class.java)
    private val mutexMock = mock(ReentrantLock::class.java)
    private val visitedURLsRepository = VisitedURLsRepository(mutexMock, jedisMock)

    private val visitedURLs = "visitedURLs"

    @Test
    fun `returns only new urls`() {
        val unfilteredURLs = listOf(URLInfo("newURL1"), URLInfo("existingURL"), URLInfo("existingURL2"), URLInfo("newURL2"))
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[0].hash)).thenReturn(null)
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[1].hash)).thenReturn(1)
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[2].hash)).thenReturn(2)
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[3].hash)).thenReturn(null)

        val expectedResult = listOf(unfilteredURLs[0], unfilteredURLs[3])
        val result = visitedURLsRepository.getOnlyNewURLs(unfilteredURLs)

        assertEquals(expectedResult, result)
        verify(mutexMock).lock()
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }
}