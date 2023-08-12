package frontier

import communicationManager.CommunicationManager
import dto.FormattedURL
import dto.FrontierQueue
import dto.FrontierRecord
import frontier.Configuration.DEFAULT_PATH
import frontier.Configuration.FRONTIER_KEY
import frontier.Configuration.QUEUES_KEY
import interfaces.IFrontierRedis
import mu.KotlinLogging
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPooled
import redis.clients.jedis.Transaction
import redisConnector.RedisConnector
import java.util.concurrent.locks.ReentrantLock


object FrontierRedis: IFrontierRedis {
    private val mutex = ReentrantLock()
    private val logger = KotlinLogging.logger("Frontier")
    private val communicationManager = CommunicationManager
    private val jedis = RedisConnector.getJedis()

    init {
        jedis.set(FRONTIER_KEY, QUEUES_KEY)
    }

    override fun updateOrCreateQueue(host: String, formattedURL: FormattedURL) {
        mutex.lock()
        try {
            val frontierRecord = FrontierRecord(formattedURL)
            if(isQueueDefined(host)){
                updateQueue(host, frontierRecord)
            } else{
                createQueue(host, frontierRecord)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun isQueueDefined(host: String): Boolean{
        return jedis.lpos(DEFAULT_PATH, host) != null
    }

    private fun updateQueue(host: String, frontierRecord: FrontierRecord) {
        jedis.rpush("$DEFAULT_PATH:$host", frontierRecord.getURL())
    }

    private fun createQueue(host: String, frontierRecord: FrontierRecord) {
        logger.info ("created queue with host: $host")
        jedis.lpush(DEFAULT_PATH, host)
        jedis.rpush("$DEFAULT_PATH:$host", frontierRecord.getURL())
        communicationManager.addHost(host)
    }

    override fun pullURLRecord(host: String): FrontierRecord? {
        mutex.lock()
        try{
            val url = jedis.lpop("$DEFAULT_PATH:$host")
            if(url == null){
                deleteQueue(host)
                return null
            }

            return FrontierRecord(FormattedURL(url))
        } finally {
            mutex.unlock()
        }
    }

    private fun deleteQueue(host: String){
        logger.info("removed queue with host: $host")
        jedis.lrem(DEFAULT_PATH, 1 , host)
    }
}