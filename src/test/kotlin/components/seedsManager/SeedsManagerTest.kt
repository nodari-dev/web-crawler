package components.seedsManager

import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import infrastructure.memoryGateways.RedisMemoryGateway
import storage.frontier.Frontier
import modules.SeedsManager

class SeedsManagerTest {
    private val crawlingManager = SeedsManager

    private val mockFrontier = Mockito.mock(Frontier::class.java)
    private val mockLogger = Mockito.mock(KotlinLogging.logger("CrawlingManager")::class.java)
    private val jedisMock = Mockito.mock(RedisMemoryGateway::class.java)

    init {
        crawlingManager.setup(mockLogger, mockFrontier, jedisMock)
    }

    @Test
    fun `starts crawling correct with clearing all data before`(){
        val seeds = listOf("https://fucking-internet.com/bruh", "https://good-internet.com/bruh")
        crawlingManager.startCrawling(seeds)

        verify(jedisMock).clear()
        verify(mockFrontier).updateOrCreateQueue("https://fucking-internet.com", core.dto.WebLink(seeds[0]).url)
        verify(mockFrontier).updateOrCreateQueue("https://good-internet.com", core.dto.WebLink(seeds[1]).url)
    }

    @Test
    fun `does not start crawling when seeds are empty`(){
        val seeds = emptyList<String>()
        crawlingManager.startCrawling(seeds)

        verify(jedisMock).clear()
        verify(mockLogger).error("No seed urls provided")
    }
}