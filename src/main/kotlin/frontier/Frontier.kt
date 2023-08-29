package frontier

import crawlingManager.CrawlingManager
import dto.HashedUrlPair
import frontier.Configuration.DEFAULT_PATH
import frontier.Configuration.FRONTIER_KEY
import frontier.Configuration.QUEUES_KEY
import interfaces.IFrontier
import mu.KotlinLogging
import redis.RedisConnector
import storage.RedisStorageUtils
import java.util.concurrent.locks.ReentrantLock

object Frontier: IFrontier{
    private val crawlingManager = CrawlingManager
    private val mutex = ReentrantLock()
    private val logger = KotlinLogging.logger("Frontier")
    private val redisStorageUtils = RedisStorageUtils()
    private val jedis = RedisConnector.getJedis()

    init {
        jedis.set(FRONTIER_KEY, QUEUES_KEY)
    }

    /**
     * Sends a new URL to the frontier queue for the specified host.
     * @param host The host to which the URL belongs.
     * @param hashedUrlPair The HashedUrlPair to be added to the queue.
     */
    override fun updateOrCreateQueue(host: String, hashedUrlPair: HashedUrlPair) {
        mutex.lock()
        try {
            if(isQueueDefinedForHost(host)){
                updateQueue(host, hashedUrlPair)
            } else{
                createQueue(host, hashedUrlPair)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun isQueueDefinedForHost(host: String): Boolean{
        return jedis.lpos(DEFAULT_PATH, host) != null
    }

    private fun updateQueue(host: String, hashedUrlPair: HashedUrlPair) {
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        jedis.rpush(path, hashedUrlPair.url)
    }

    private fun createQueue(host: String, hashedUrlPair: HashedUrlPair) {
        logger.info ("created queue with host: $host")
        jedis.lpush(DEFAULT_PATH, host)

        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        jedis.rpush(path, hashedUrlPair.url)
        crawlingManager.requestCrawlerInitialization(host)
    }

    /**
     * Requests a URL from the frontier for the specified host.
     * @param host The host for which to request a URL.
     * @return The pulled HashedUrlPair.
     */
    override fun pullURL(host: String): HashedUrlPair {
        mutex.lock()
        try{
            val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
            return HashedUrlPair(jedis.lpop(path))
        } finally {
            mutex.unlock()
        }
    }

    /**
     * Checks if the frontier queue for the specified host is empty.
     * Deletes queue if it is empty
     * @param host The host for which to check the queue emptiness.
     * @return `true` if the queue is empty, `false` otherwise.
     */
    fun isQueueEmpty(host: String): Boolean{
        mutex.lock()
        try{
            val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
            val isEmpty = jedis.lrange(path, 0 , 1).size == 0
            if(isEmpty){
                deleteQueue(host)
            }
            return isEmpty
        } finally {
            mutex.unlock()
        }
    }

    private fun deleteQueue(host: String){
        logger.info("removed queue with host: $host")
        jedis.lrem(DEFAULT_PATH, 1 , host)
    }
}