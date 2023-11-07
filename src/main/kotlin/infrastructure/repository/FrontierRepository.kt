package infrastructure.repository

import application.interfaces.memoryGateways.IFrontierRepository
import redis.clients.jedis.Jedis
import java.util.concurrent.locks.ReentrantLock

class FrontierRepository(
    private val mutex: ReentrantLock,
    private val jedis: Jedis
    ): IFrontierRepository {

    private val table = "frontier"
    private val field = "queues"

    override fun create(host: String, list: List<String>) {
        mutex.lock()
        try{
            jedis.use { jedis ->
                list.forEach { item ->
                    jedis.rpush(host, item)
                }
                val field = getQFieldName(host)
                jedis.hset(table, field, host)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun getQueuesInfo(): MutableMap<String, String>? {
        mutex.lock()
        try{
            jedis.use { jedis ->
                return jedis.hgetAll(table)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun isQueueDefined(host: String): Boolean {
        mutex.lock()
        try{
            jedis.use { jedis ->
                return jedis.hgetAll(table).containsKey(getQFieldName(host))
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun getLastItem(host: String): String? {
        mutex.lock()
        try{
            jedis.use { jedis ->
                return jedis.lpop(host)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun update(host: String, list: List<String>) {
        mutex.lock()
        try{
            jedis.use { jedis ->
                list.forEach { item ->
                    jedis.rpush(host, item)
                }
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun assignCrawler(host: String, crawlerId: String) {
        mutex.lock()
        try{
            jedis.use{ jedis ->
                jedis.rpush(getCrawlersFieldName(host), crawlerId)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun unassignCrawler(host: String, crawlerId: String) {
        mutex.lock()
        try{
            jedis.use{ jedis ->
                jedis.lrem(getCrawlersFieldName(host),0, crawlerId)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun delete(host: String) {
        mutex.lock()
        try{
            jedis.use{ jedis ->
                jedis.hdel(table, getQFieldName(host))
                jedis.hdel(table, getCrawlersFieldName(host))
                jedis.del(host)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun getQFieldName(host: String): String{
        return "$host-urls"
    }

    private fun getCrawlersFieldName(host: String): String{
        return "$host-crawlers"
    }

    override fun clear(){
        mutex.lock()
        try{
            jedis.flushAll()
            jedis.close()
        } finally {
            mutex.unlock()
        }
    }
}