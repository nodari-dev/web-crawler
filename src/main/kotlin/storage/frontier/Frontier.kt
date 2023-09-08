package storage.frontier

import crawler.CrawlersFactory
import dto.HashedURLPair
import storage.frontier.Configuration.DEFAULT_PATH
import storage.frontier.Configuration.FRONTIER_KEY
import storage.frontier.Configuration.QUEUES_KEY
import interfaces.IFrontier
import mu.KotlinLogging
import redis.RedisConnector
import storage.RedisStorageUtils
import java.util.concurrent.locks.ReentrantLock

object Frontier: IFrontier{
    private val mutex = ReentrantLock()
    private val redisStorageUtils = RedisStorageUtils()
    private val jedis = RedisConnector.getJedis()
    var logger = KotlinLogging.logger("Frontier")
    var crawlersFactory = CrawlersFactory

    init {
        jedis.set(FRONTIER_KEY, QUEUES_KEY)
    }

    override fun updateOrCreateQueue(host: String, url: String) {
        mutex.lock()
        try {
            if(isQueueDefinedForHost(host)){
                updateQueue(host, url)
            } else{
                createQueue(host, url)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun isQueueDefinedForHost(host: String): Boolean{
        return jedis.lpos(DEFAULT_PATH, host) != null
    }

    private fun updateQueue(host: String, url: String) {
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        jedis.rpush(path, url)
    }

    private fun createQueue(host: String, url: String) {
        logger.info ("created queue with host: $host")
        jedis.lpush(DEFAULT_PATH, host)

        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        jedis.rpush(path, url)
        crawlersFactory.requestCrawlerInitialization(host)
    }

    override fun pullURL(host: String): HashedURLPair {
        mutex.lock()
        try{
            val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
            return HashedURLPair(jedis.lpop(path))
        } finally {
            mutex.unlock()
        }
    }

    override fun isQueueEmpty(host: String): Boolean{
        mutex.lock()
        try {
            val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
            return jedis.lrange(path, 0, 1).size == 0
        } finally {
            mutex.unlock()
        }
    }

    override fun deleteQueue(host: String){
        mutex.lock()
        try {
            logger.info("removed queue with host: $host")
            jedis.del(redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host)))
            jedis.lrem(DEFAULT_PATH, 1 , host)
        } finally {
            mutex.unlock()
        }
    }
}