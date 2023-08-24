package frontier

import communication.CommunicationManager
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
    private val mutex = ReentrantLock()
    private val logger = KotlinLogging.logger("Frontier")
    private val redisStorageUtils = RedisStorageUtils()
    private val communicationManager = CommunicationManager
    private val jedis = RedisConnector.getJedis()

    init {
        jedis.set(FRONTIER_KEY, QUEUES_KEY)
    }

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
        jedis.rpush(path, hashedUrlPair.value)
    }

    private fun createQueue(host: String, hashedUrlPair: HashedUrlPair) {
        logger.info ("created queue with host: $host")
        jedis.lpush(DEFAULT_PATH, host)

        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        jedis.rpush(path, hashedUrlPair.value)
        communicationManager.requestCrawlerInitialization(host)
    }

    override fun pullURL(host: String): HashedUrlPair {
        mutex.lock()
        try{
            val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
            return HashedUrlPair(jedis.lpop(path))
        } finally {
            mutex.unlock()
        }
    }

    fun isQueueEmpty(host: String): Boolean{
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        val isEmpty = jedis.lrange(path, 0 , 1).size == 0
        if(isEmpty){
            deleteQueue(host)
        }
        return isEmpty
    }

    private fun deleteQueue(host: String){
        logger.info("removed queue with host: $host")
        jedis.lrem(DEFAULT_PATH, 1 , host)
    }
}