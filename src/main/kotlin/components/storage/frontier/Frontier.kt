package components.storage.frontier


import mu.KLogger
import mu.KotlinLogging
import adatapters.gateways.memoryGateways.RedisMemoryGateway
import adatapters.gateways.memoryGateways.RedisStorageUtils
import components.crawler.CrawlersManager
import components.storage.frontier.Configuration.DEFAULT_PATH
import components.storage.frontier.Configuration.FRONTIER_KEY
import components.storage.frontier.Configuration.QUEUES_KEY
import core.interfaces.components.IFrontier

object Frontier: IFrontier {
    private val redisStorageUtils = RedisStorageUtils()
    private var jedis = RedisMemoryGateway
    private var logger = KotlinLogging.logger("Frontier")
    private var crawlersManager = CrawlersManager

    init {
        jedis.createEntry(FRONTIER_KEY, QUEUES_KEY)
    }

    override fun updateOrCreateQueue(host: String, url: String) {
        if(isQueueDefinedForHost(host)){
            updateQueue(host, url)
        } else{
            createQueue(host, url)
        }
    }

    private fun isQueueDefinedForHost(host: String): Boolean{
        return jedis.isEntryKeyDefined(DEFAULT_PATH, host)
    }

    private fun updateQueue(host: String, url: String) {
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, host)
        jedis.updateEntry(path, url)
    }

    private fun createQueue(host: String, url: String) {
        logger.info ("created queue with host: $host")
        jedis.createEntry(DEFAULT_PATH, host)

        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, host)
        jedis.updateEntry(path, url)
        crawlersManager.requestCrawlerInitialization(host)
    }

    override fun pullURL(host: String): core.dto.HashedURLPair {
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, host)
        return core.dto.HashedURLPair(jedis.getFirstEntryItem(path))
    }

    override fun isQueueEmpty(host: String): Boolean{
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, host)
        return jedis.checkEntryEmptiness(path)
    }

    override fun deleteQueue(host: String){
        logger.info("removed queue with host: $host")
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, host)
        jedis.deleteEntry(DEFAULT_PATH, path ,host)
    }

    fun setupTest(jedisMock: RedisMemoryGateway, loggerMock: KLogger, crawlersManagerMock: CrawlersManager){
        jedis = jedisMock
        logger = loggerMock
        crawlersManager = crawlersManagerMock
    }
}