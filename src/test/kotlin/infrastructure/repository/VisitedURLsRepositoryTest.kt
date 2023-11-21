package infrastructure.repository

import core.dto.URLInfo
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
        val unfilteredURLs = listOf(URLInfo("newURL1"), URLInfo("existingURL"), URLInfo("existingURL2"), URLInfo("newURL2"))
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[0].link)).thenReturn(null)
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[1].link)).thenReturn(1)
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[2].link)).thenReturn(2)
        `when`(jedisMock.lpos(visitedURLs, unfilteredURLs[3].link)).thenReturn(null)

        val expectedResult = listOf(unfilteredURLs[0], unfilteredURLs[3])
        val result = visitedURLsRepository.getOnlyNewURLs(unfilteredURLs)

        assertEquals(expectedResult, result)
        verify(mutexMock).lock()
        verify(jedisMock).close()
        verify(mutexMock).unlock()
    }
}