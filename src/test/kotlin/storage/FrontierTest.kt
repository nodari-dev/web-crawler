package storage

import crawler.CrawlersFactory
import dto.HashedUrlPair
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
    private val hashedUrlPair = HashedUrlPair("$host/demon/")
    private val hashedUrlPairTwo = HashedUrlPair("$host/hell/")

    private val anotherHost = "https://hell.com"
    private val anotherUrl = HashedUrlPair("$anotherHost/hello")


    private lateinit var crawlersFactory: CrawlersFactory

    @BeforeEach
    fun `flush all`() {
        crawlersFactory = mock(CrawlersFactory::class.java)
        jedis.flushAll()
    }

    @Test
    fun `creates queue and adds url`() {
        frontier.updateOrCreateQueue(host, hashedUrlPair)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl)

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
    }

    @Test
    fun `updates current queue`() {
        frontier.updateOrCreateQueue(host, hashedUrlPair)
        frontier.updateOrCreateQueue(host, hashedUrlPairTwo)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl)

        Assertions.assertEquals(2, testUtils.getDefaultPathContent(DEFAULT_PATH)!!.size)
        Assertions.assertEquals(mutableListOf(anotherHost, host), testUtils.getDefaultPathContent(DEFAULT_PATH))

        Assertions.assertEquals(
            mutableListOf(hashedUrlPair.url, hashedUrlPairTwo.url),
            testUtils.getDefaultPathChildContent(DEFAULT_PATH, host)
        )

        Assertions.assertEquals(
            mutableListOf(anotherUrl.url),
            testUtils.getDefaultPathChildContent(DEFAULT_PATH, anotherHost)
        )
    }

    @Test
    fun `returns url from queue`() {
        frontier.updateOrCreateQueue(host, hashedUrlPair)
        frontier.updateOrCreateQueue(host, hashedUrlPairTwo)

        Assertions.assertEquals(2, testUtils.getDefaultPathChildContent(DEFAULT_PATH, host)!!.size)
        Assertions.assertEquals(hashedUrlPair, frontier.pullURL(host))
        Assertions.assertEquals(1, testUtils.getDefaultPathChildContent(DEFAULT_PATH, host)!!.size)
        Assertions.assertEquals(mutableListOf(hashedUrlPairTwo.url), testUtils.getDefaultPathChildContent(DEFAULT_PATH, host))
    }

    @Test
    fun `checks if queue is empty`() {
        frontier.updateOrCreateQueue(host, hashedUrlPair)
        Assertions.assertEquals(false ,frontier.isQueueEmpty(host))
        Assertions.assertEquals(true ,frontier.isQueueEmpty(anotherHost))
    }

    @Test
    fun `deletes queue`() {
        frontier.updateOrCreateQueue(host, hashedUrlPair)
        frontier.updateOrCreateQueue(anotherHost, anotherUrl)

        frontier.deleteQueue(anotherHost)
        Assertions.assertEquals(1, testUtils.getDefaultPathContent(DEFAULT_PATH)!!.size)
        Assertions.assertEquals(mutableListOf<String>(), testUtils.getDefaultPathChildContent(DEFAULT_PATH, anotherHost))
    }
}