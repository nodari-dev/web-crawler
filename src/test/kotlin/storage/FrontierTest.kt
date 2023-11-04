package storage

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import infrastructure.repository.RedisRepository
import modules.CrawlersManager
import storage.frontier.Configuration.DEFAULT_PATH
import storage.frontier.Frontier

class FrontierTest {
    private val frontier = Frontier
    private val host = "https://host.com"
    private val URLData = core.dto.URLData("$host/demon/")
    private val URLDataTwo = core.dto.URLData("$host/hell/")

    private val anotherHost = "https://hell.com"
    private val anotherUrl = core.dto.URLData("$anotherHost/hello")

    private val mockCrawlersManager = mock(CrawlersManager::class.java)
    private val mockLogger = mock(KotlinLogging.logger("Frontier")::class.java)
    private val jedisMock = mock(RedisRepository::class.java)


    init{
        frontier.setupTest(jedisMock, mockLogger, mockCrawlersManager)
    }

    @Test
    fun `creates queue and adds url`() {
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false)
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, anotherHost)).thenReturn(false)

        frontier.updateOrCreateQueue(host, URLData.url)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl.url)

        verify(mockLogger).info("created queue with host: $host")
        verify(mockLogger).info("created queue with host: $anotherHost")

        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).createEntry(DEFAULT_PATH, anotherHost)

        verify(jedisMock).updateEntry(DEFAULT_PATH, host, URLData.url)
        verify(jedisMock).updateEntry(DEFAULT_PATH, anotherHost, anotherUrl.url)

        verify(mockCrawlersManager).requestCrawlerInitialization(host)
        verify(mockCrawlersManager).requestCrawlerInitialization(anotherHost)
    }

    @Test
    fun `updates current queue`() {
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false, true)
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, anotherHost)).thenReturn(false)

        frontier.updateOrCreateQueue(host, URLData.url)
        frontier.updateOrCreateQueue(host, URLDataTwo.url)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl.url)


        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, URLData.url)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, URLDataTwo.url)

        verify(jedisMock).createEntry(DEFAULT_PATH, anotherHost)
        verify(jedisMock).updateEntry(DEFAULT_PATH, anotherHost, anotherUrl.url)
    }

    @Test
    fun `returns url from queue`() {
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false, true)
        `when`(jedisMock.getFirstEntryItem(DEFAULT_PATH, host)).thenReturn(URLData.url, URLDataTwo.url)

        frontier.updateOrCreateQueue(host, URLData.url)
        frontier.updateOrCreateQueue(host, URLDataTwo.url)
        val result = frontier.pullURL(host)
        val resultTwo = frontier.pullURL(host)

        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, URLData.url)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, URLDataTwo.url)

        verify(jedisMock, times(2)).getFirstEntryItem(DEFAULT_PATH, host)

        Assertions.assertEquals(URLData, result)
        Assertions.assertEquals(URLDataTwo, resultTwo)
    }

    @Test
    fun `checks if queue is empty`() {
        `when`(jedisMock.checkEntryEmptiness(DEFAULT_PATH, host)).thenReturn(false)
        `when`(jedisMock.checkEntryEmptiness(DEFAULT_PATH, anotherHost)).thenReturn(true)

        frontier.updateOrCreateQueue(host, URLData.url)
        val result = frontier.isQueueEmpty(host)
        val resultTwo = frontier.isQueueEmpty(anotherHost)

        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, URLData.url)

        Assertions.assertEquals(false ,result)
        Assertions.assertEquals(true, resultTwo)
    }

    @Test
    fun `deletes queue`() {
        frontier.updateOrCreateQueue(host, URLData.url)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl.url)

        frontier.deleteQueue(anotherHost)

        verify(mockLogger).info("removed queue with host: $anotherHost")

        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, URLData.url)
        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, anotherHost, anotherUrl.url)

        verify(jedisMock).deleteEntry(DEFAULT_PATH, anotherHost)
    }
}