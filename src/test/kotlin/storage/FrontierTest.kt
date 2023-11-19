package storage

import core.dto.URLInfo
import infrastructure.repository.FrontierRepository
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals

class FrontierTest {
    private val frontierRepositoryMock = mock(FrontierRepository::class.java)
    private val logger = mock(KotlinLogging.logger("Frontier")::class.java)
    private val frontier = Frontier(frontierRepositoryMock, logger)

    @Test
    fun `update works correct`(){
        val list = listOf(URLInfo("some-url"))
        val host = "somehost"
        val newHost = "mew-host"

        // act
        frontier.update(host, list)
        frontier.update(newHost, list)
        verify(frontierRepositoryMock).update(host, list)
        verify(frontierRepositoryMock).update(newHost, list)
    }

    @Test
    fun `pulls url from queue`(){
        val existingHost = "somehost"
        val newHost = "mew-host"

        `when`(frontierRepositoryMock.get(existingHost)).thenReturn("url")
        `when`(frontierRepositoryMock.get(newHost)).thenReturn(null)

        val resultWithURL = frontier.pullFrom(existingHost)
        assertEquals("url", resultWithURL)

        val resultWithNull = frontier.pullFrom(newHost)
        assertEquals(null, resultWithNull)
    }
//
//    @Test
//    fun `deletes queue`(){
//        val existingHost = "somehost"
//
//        frontier.deleteQueue(existingHost)
//        verify(frontierRepositoryMock).delete(existingHost)
//        verify(logger).info ("removed queue with host: $existingHost")
//
//    }
//
    @Test
    fun `assigns crawler to queue`(){
        val host = "somehost"
        val crawlerId = 12

        frontier.assign(crawlerId, host)
        verify(frontierRepositoryMock).assignCrawler(crawlerId, host )
        verify(logger).info ("assigned crawler $crawlerId to $host")
    }

    @Test
    fun `unassigns crawler from queue`(){
        val host = "somehost"
        val crawlerId = 12

        frontier.unassign(crawlerId, host)
        verify(frontierRepositoryMock).unassignCrawler(crawlerId, host)
        verify(logger).info ("removed crawler $crawlerId from $host")
    }
}