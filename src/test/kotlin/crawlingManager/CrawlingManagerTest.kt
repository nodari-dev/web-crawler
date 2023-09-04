package crawlingManager

import dataExtractor.DataExtractor
import dto.HashedURLPair
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import redis.RedisConnector
import storage.frontier.Frontier

class CrawlingManagerTest {
    private val crawlingManager = CrawlingManager

    private val mockFrontier = Mockito.mock(Frontier::class.java)
    private val mockDataExtractor = Mockito.mock(DataExtractor::class.java)
    private val mockLogger = Mockito.mock(KotlinLogging.logger("CrawlingManager")::class.java)
    private val mockJedis = Mockito.mock(RedisConnector::class.java).getJedis()

    init {
        crawlingManager.frontier = mockFrontier
        crawlingManager.dataExtractor = mockDataExtractor
        crawlingManager.logger = mockLogger
        crawlingManager.jedis = mockJedis
    }

    @Test
    fun `starts crawling correct with clearing all data before`(){
        val seeds = listOf("https://fucking-internet.com/bruh", "https://good-internet.com/bruh")
        crawlingManager.startCrawling(seeds)

//        verify(mockJedis.flushAll(), times(1))
        verify(mockFrontier).updateOrCreateQueue("https://fucking-internet.com", HashedURLPair(seeds[0]))
        verify(mockFrontier).updateOrCreateQueue("https://good-internet.com", HashedURLPair(seeds[1]))
    }

    @Test
    fun `does not start crawling when seeds are empty`(){
        val seeds = emptyList<String>()
        crawlingManager.startCrawling(seeds)

        verify(mockLogger).error("No seed urls provided")
    }
}