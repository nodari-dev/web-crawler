package storage

import core.dto.URLData
import infrastructure.repository.FrontierRepository
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import storage.frontier.FrontierV2
import kotlin.test.assertEquals

class FrontierV2Test {
    private val frontierRepositoryMock = mock(FrontierRepository::class.java)
    private val logger = mock(KotlinLogging.logger("Frontier")::class.java)
    private val frontier = FrontierV2(frontierRepositoryMock, logger)

    @Test
    fun `updateOrCreateQueue works correct`(){
        val list = listOf("list")
        val existingHost = "somehost"
        val newHost = "mew-host"

        `when`(frontierRepositoryMock.isQueueDefined(existingHost)).thenReturn(true)
        `when`(frontierRepositoryMock.isQueueDefined(newHost)).thenReturn(false)

        // act
        frontier.updateOrCreateQueue(existingHost, list)
        verify(frontierRepositoryMock).update(existingHost, list)

        frontier.updateOrCreateQueue(newHost, list)
        verify(frontierRepositoryMock).create(newHost, list)
        verify(logger).info ("created queue with host: $newHost")
    }

    @Test
    fun `pulls url from queue`(){
        val existingHost = "somehost"
        val newHost = "mew-host"

        `when`(frontierRepositoryMock.getLastItem(existingHost)).thenReturn("url")
        `when`(frontierRepositoryMock.getLastItem(newHost)).thenReturn(null)

        val resultWithURL = frontier.pullWebLink(existingHost)
        assertEquals(URLData("url"), resultWithURL)

        val resultWithNull = frontier.pullWebLink(newHost)
        assertEquals(null, resultWithNull)
    }

    @Test
    fun `deletes queue`(){
        val existingHost = "somehost"

        frontier.deleteQueue(existingHost)
        verify(frontierRepositoryMock).delete(existingHost)
        verify(logger).info ("removed queue with host: $existingHost")

    }

    @Test
    fun `assigns crawler to queue`(){
        val host = "somehost"
        val crawlerId = "crawlerId"

        frontier.assignCrawler(host, crawlerId)
        verify(frontierRepositoryMock).assignCrawler(host, crawlerId)
        verify(logger).info ("assigned crawler $crawlerId to queue with host: $host")
    }

    @Test
    fun `unassigns crawler from queue`(){
        val host = "somehost"
        val crawlerId = "crawlerId"

        frontier.unassignCrawler(host, crawlerId)
        verify(frontierRepositoryMock).unassignCrawler(host, crawlerId)
        verify(logger).info ("unassigned crawler $crawlerId from queue with host: $host")
    }
}