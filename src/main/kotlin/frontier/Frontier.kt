package frontier

import communication.CommunicationManager
import dto.FormattedURL
import frontier.Configuration.DEFAULT_PATH
import frontier.Configuration.FRONTIER_KEY
import frontier.Configuration.QUEUES_KEY
import interfaces.IFrontier
import mu.KotlinLogging
import redis.RedisConnector
import storage.StorageUtils
import java.util.concurrent.locks.ReentrantLock


object Frontier: IFrontier{
    private val mutex = ReentrantLock()
    private val logger = KotlinLogging.logger("Frontier")
    private val storageUtils = StorageUtils()
    private val communicationManager = CommunicationManager
    private val jedis = RedisConnector.getJedis()

    init {
        jedis.set(FRONTIER_KEY, QUEUES_KEY)
    }

    override fun updateOrCreateQueue(host: String, formattedURL: FormattedURL) {
        mutex.lock()
        try {
            if(isQueueDefinedForHost(host)){
                updateQueue(host, formattedURL)
            } else{
                createQueue(host, formattedURL)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun isQueueDefinedForHost(host: String): Boolean{
        return jedis.lpos(DEFAULT_PATH, host) != null
    }

    private fun updateQueue(host: String, formattedURL: FormattedURL) {
        val path = storageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        jedis.rpush(path, formattedURL.value)
    }

    private fun createQueue(host: String, formattedURL: FormattedURL) {
        logger.info ("created queue with host: $host")
        jedis.lpush(DEFAULT_PATH, host)

        val path = storageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        jedis.rpush(path, formattedURL.value)
        communicationManager.addHost(host)
    }

    override fun pullURL(host: String): FormattedURL? {
        mutex.lock()
        try{
            val path = storageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
            val url = jedis.lpop(path)
            if(url == null){
                deleteQueue(host)
                return null
            }

            return FormattedURL(url)
        } finally {
            mutex.unlock()
        }
    }

    private fun deleteQueue(host: String){
        logger.info("removed queue with host: $host")
        jedis.lrem(DEFAULT_PATH, 1 , host)
    }
}