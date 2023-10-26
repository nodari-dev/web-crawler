package components.storage

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import adatapters.gateways.memoryGateways.RedisMemoryGateway
import components.crawler.CrawlersManager
import components.storage.frontier.Configuration.DEFAULT_PATH
import components.storage.frontier.Frontier

class FrontierTest {
    private val frontier = Frontier
    private val host = "https://host.com"
    private val webLink = core.dto.WebLink("$host/demon/")
    private val webLinkTwo = core.dto.WebLink("$host/hell/")

    private val anotherHost = "https://hell.com"
    private val anotherUrl = core.dto.WebLink("$anotherHost/hello")

    private val mockCrawlersManager = mock(CrawlersManager::class.java)
    private val mockLogger = mock(KotlinLogging.logger("Frontier")::class.java)
    private val jedisMock = mock(RedisMemoryGateway::class.java)


    init{
        frontier.setupTest(jedisMock, mockLogger, mockCrawlersManager)
    }

    @Test
    fun `creates queue and adds url`() {
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false)
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, anotherHost)).thenReturn(false)

        frontier.updateOrCreateQueue(host, webLink.url)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl.url)

        verify(mockLogger).info("created queue with host: $host")
        verify(mockLogger).info("created queue with host: $anotherHost")

        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).createEntry(DEFAULT_PATH, anotherHost)

        verify(jedisMock).updateEntry(DEFAULT_PATH, host, webLink.url)
        verify(jedisMock).updateEntry(DEFAULT_PATH, anotherHost, anotherUrl.url)

        verify(mockCrawlersManager).requestCrawlerInitialization(host)
        verify(mockCrawlersManager).requestCrawlerInitialization(anotherHost)
    }

    @Test
    fun `updates current queue`() {
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false, true)
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, anotherHost)).thenReturn(false)

        frontier.updateOrCreateQueue(host, webLink.url)
        frontier.updateOrCreateQueue(host, webLinkTwo.url)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl.url)


        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, webLink.url)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, webLinkTwo.url)

        verify(jedisMock).createEntry(DEFAULT_PATH, anotherHost)
        verify(jedisMock).updateEntry(DEFAULT_PATH, anotherHost, anotherUrl.url)
    }

    @Test
    fun `returns url from queue`() {
        `when`(jedisMock.isEntryKeyDefined(DEFAULT_PATH, host)).thenReturn(false, true)
        `when`(jedisMock.getFirstEntryItem(DEFAULT_PATH, host)).thenReturn(webLink.url, webLinkTwo.url)

        frontier.updateOrCreateQueue(host, webLink.url)
        frontier.updateOrCreateQueue(host, webLinkTwo.url)
        val result = frontier.pullURL(host)
        val resultTwo = frontier.pullURL(host)

        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, webLink.url)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, webLinkTwo.url)

        verify(jedisMock, times(2)).getFirstEntryItem(DEFAULT_PATH, host)

        Assertions.assertEquals(webLink, result)
        Assertions.assertEquals(webLinkTwo, resultTwo)
    }

    @Test
    fun `checks if queue is empty`() {
        `when`(jedisMock.checkEntryEmptiness(DEFAULT_PATH, host)).thenReturn(false)
        `when`(jedisMock.checkEntryEmptiness(DEFAULT_PATH, anotherHost)).thenReturn(true)

        frontier.updateOrCreateQueue(host, webLink.url)
        val result = frontier.isQueueEmpty(host)
        val resultTwo = frontier.isQueueEmpty(anotherHost)

        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, webLink.url)

        Assertions.assertEquals(false ,result)
        Assertions.assertEquals(true, resultTwo)
    }

    @Test
    fun `deletes queue`() {
        frontier.updateOrCreateQueue(host, webLink.url)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl.url)

        frontier.deleteQueue(anotherHost)

        verify(mockLogger).info("removed queue with host: $anotherHost")

        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, host, webLink.url)
        verify(jedisMock).createEntry(DEFAULT_PATH, host)
        verify(jedisMock).updateEntry(DEFAULT_PATH, anotherHost, anotherUrl.url)

        verify(jedisMock).deleteEntry(DEFAULT_PATH, anotherHost)
    }
}