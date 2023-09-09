package storage.frontier

import crawler.CrawlersFactory
import dto.HashedURLPair
import storage.frontier.Configuration.DEFAULT_PATH
import storage.frontier.Configuration.FRONTIER_KEY
import storage.frontier.Configuration.QUEUES_KEY
import interfaces.IFrontier
import mu.KotlinLogging
import redis.RedisManager
import redis.RedisStorageUtils
import java.util.concurrent.locks.ReentrantLock

object Frontier: IFrontier{
    private val redisStorageUtils = RedisStorageUtils()
    private val jedis = RedisManager
    var logger = KotlinLogging.logger("Frontier")
    var crawlersFactory = CrawlersFactory

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
        crawlersFactory.requestCrawlerInitialization(host)
    }

    override fun pullURL(host: String): HashedURLPair {
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, host)
        return HashedURLPair(jedis.getFirstEntryItem(path))
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
}