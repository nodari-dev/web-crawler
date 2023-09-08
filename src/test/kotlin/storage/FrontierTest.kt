package storage

import crawler.CrawlersFactory
import dto.HashedURLPair
import mu.KotlinLogging
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import redis.RedisConnector
import storage.frontier.Configuration.DEFAULT_PATH
import storage.frontier.Frontier

class FrontierTest {
    private val frontier = Frontier
    private val jedis = RedisConnector.getJedis()
    private val testUtils = TestUtils()
    private val host = "https://host.com"
    private val hashedUrlPair = HashedURLPair("$host/demon/")
    private val hashedURLPairTwo = HashedURLPair("$host/hell/")

    private val anotherHost = "https://hell.com"
    private val anotherUrl = HashedURLPair("$anotherHost/hello")

    private val mockCrawlersFactory = mock(CrawlersFactory::class.java)
    private val mockLogger = mock(KotlinLogging.logger("Frontier")::class.java)


    init{
        frontier.crawlersFactory = mockCrawlersFactory
        frontier.logger = mockLogger
    }

    @BeforeEach
    fun `flush all`() {
        jedis.flushAll()
    }

    @Test
    fun `creates queue and adds url`() {

        frontier.updateOrCreateQueue(host, hashedUrlPair.url)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl.url)

        Assertions.assertEquals(2, testUtils.getDefaultPathContent(DEFAULT_PATH)!!.size)
        Assertions.assertEquals(mutableListOf(anotherHost, host), testUtils.getDefaultPathContent(DEFAULT_PATH))
        Assertions.assertEquals(
            mutableListOf(hashedUrlPair.url),
            testUtils.getDefaultPathChildContent(DEFAULT_PATH, host)
        )

        Assertions.assertEquals(
            mutableListOf(anotherUrl.url),
            testUtils.getDefaultPathChildContent(DEFAULT_PATH, anotherHost)
        )

        verify(mockCrawlersFactory).requestCrawlerInitialization(host)
        verify(mockCrawlersFactory).requestCrawlerInitialization(anotherHost)
        verify(mockLogger).info("created queue with host: $host")
        verify(mockLogger).info("created queue with host: $anotherHost")
    }

    @Test
    fun `updates current queue`() {
        frontier.updateOrCreateQueue(host, hashedUrlPair.url)
        frontier.updateOrCreateQueue(host, hashedURLPairTwo.url)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl.url)

        Assertions.assertEquals(2, testUtils.getDefaultPathContent(DEFAULT_PATH)!!.size)
        Assertions.assertEquals(mutableListOf(anotherHost, host), testUtils.getDefaultPathContent(DEFAULT_PATH))

        Assertions.assertEquals(
            mutableListOf(hashedUrlPair.url, hashedURLPairTwo.url),
            testUtils.getDefaultPathChildContent(DEFAULT_PATH, host)
        )

        Assertions.assertEquals(
            mutableListOf(anotherUrl.url),
            testUtils.getDefaultPathChildContent(DEFAULT_PATH, anotherHost)
        )
    }

    @Test
    fun `returns url from queue`() {
        frontier.updateOrCreateQueue(host, hashedUrlPair.url)
        frontier.updateOrCreateQueue(host, hashedURLPairTwo.url)

        Assertions.assertEquals(2, testUtils.getDefaultPathChildContent(DEFAULT_PATH, host)!!.size)
        Assertions.assertEquals(hashedUrlPair, frontier.pullURL(host))
        Assertions.assertEquals(1, testUtils.getDefaultPathChildContent(DEFAULT_PATH, host)!!.size)
        Assertions.assertEquals(mutableListOf(hashedURLPairTwo.url), testUtils.getDefaultPathChildContent(DEFAULT_PATH, host))
    }

    @Test
    fun `checks if queue is empty`() {
        frontier.updateOrCreateQueue(host, hashedUrlPair.url)
        Assertions.assertEquals(false ,frontier.isQueueEmpty(host))
        Assertions.assertEquals(true ,frontier.isQueueEmpty(anotherHost))
    }
//
//    @Test
//    fun `deletes queue`() {
//        frontier.updateOrCreateQueue(host, hashedUrlPair.url)
//        frontier.updateOrCreateQueue(anotherHost, anotherUrl.url)
//
//        frontier.deleteQueue(anotherHost)
//        Assertions.assertEquals(1, testUtils.getDefaultPathContent(DEFAULT_PATH)!!.size)
//        Assertions.assertEquals(mutableListOf<String>(), testUtils.getDefaultPathChildContent(DEFAULT_PATH, anotherHost))
//
//        verify(mockLogger).info("removed queue with host: $anotherHost")
//    }

    @AfterEach
    fun afterEach() {
        jedis.flushAll()
    }
}