package infrastructure.repository

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import redis.clients.jedis.JedisPool
import java.util.concurrent.locks.ReentrantLock
import kotlin.test.assertEquals

class VisitedURLsRepositoryTest {
    private val jedisMock = mock(JedisPool("localhost", 6379).resource::class.java)
    private val mutexMock = mock(ReentrantLock::class.java)
    private val visitedURLsRepository = VisitedURLsRepository(mutexMock, jedisMock)

    private val visitedURLs = "visitedURLs"

    @AfterEach
    fun cleanup(){
        visitedURLsRepository.clear()
    }

    @Test
    fun `returns only new urls`() {
        val unfilteredURLs = listOf("newURL1", "existingURL", "existingURL2", "newURL2")
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[0])).thenReturn(null)
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[1])).thenReturn(1)
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[2])).thenReturn(2)
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[3])).thenReturn(null)

        val expectedResult = listOf(unfilteredURLs[0], unfilteredURLs[3])
        val result = visitedURLsRepository.getOnlyNewURLs(unfilteredURLs)

        assertEquals(expectedResult, result)
        verify(mutexMock).lock()
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }
}