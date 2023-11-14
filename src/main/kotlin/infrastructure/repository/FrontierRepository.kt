package infrastructure.repository

import application.interfaces.repository.IFrontierRepository
import redis.clients.jedis.Jedis
import java.util.concurrent.locks.ReentrantLock

class FrontierRepository(
    private val mutex: ReentrantLock,
    private val jedis: Jedis
    ): IFrontierRepository {

    private val frontier = "frontier"

    override fun update(host: String, list: List<String>){
        mutex.lock()
        try{
            jedis.use { jedis ->
                list.forEach { item ->
                    val key = getURLsKey(host)
                    jedis.rpush(key, item)
                }
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun assignCrawler(id: Int, host: String) {
        mutex.lock()
        try{
            jedis.use { jedis ->
                val key = getCrawlersKey(host)
                jedis.rpush(key, "$id")
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun unassignCrawler(id: Int, host: String) {
        mutex.lock()
        try{
            jedis.use { jedis ->
                val key = getCrawlersKey(host)
                jedis.lrem(key, 0, "$id")
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun getQueuesData(): MutableSet<String>? {
        mutex.lock()
        try{
            jedis.use { jedis ->
                val data = jedis.keys("$frontier*")
                // TODO: IMPLEMENT
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun get(host: String): String? {
        mutex.lock()
        try{
            jedis.use { jedis ->
                return jedis.lpop(getURLsKey(host))
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun getCrawlersKey(host: String): String{
        return "$frontier:$host:crawlerIds"
    }

    private fun getURLsKey(host: String): String{
        return "$frontier:$host:urls"
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