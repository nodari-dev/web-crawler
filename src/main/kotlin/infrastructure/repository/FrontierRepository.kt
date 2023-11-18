package infrastructure.repository

import core.dto.URLInfo
import infrastructure.repository.interfaces.IFrontierRepository
import redis.clients.jedis.Jedis
import java.util.concurrent.locks.ReentrantLock

class FrontierRepository(
    private val mutex: ReentrantLock,
    private val jedis: Jedis
    ): IFrontierRepository {

    private val frontier = "frontier"

    override fun update(host: String, list: List<URLInfo>){
        mutex.lock()
        try{
            jedis.use { jedis ->
                list.forEach { item ->
                    val key = getURLsKey(host)
                    jedis.rpush(key, item.link)
                }
                changeQueueAvailability(host)
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
                changeQueueAvailability(host)
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
                changeQueueAvailability(host)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun getAvailableQueue(): String? {
        mutex.lock()
        try{
            jedis.use { jedis ->
                val keys = jedis.keys("$frontier:*:available")
                var host: String? = null
                keys.forEach{item ->
                    if (jedis.get(item) == "yes"){
                        host = item.split(":")[1]
                        return@forEach
                    }
                }
                return host
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun get(host: String): URLInfo? {
        mutex.lock()
        try{
            jedis.use { jedis ->
                val url = jedis.lpop(getURLsKey(host))
                changeQueueAvailability(host)
                return if (url == null) null else URLInfo(url)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun changeQueueAvailability(host: String){
        val urlsCount = jedis.lrange(getURLsKey(host), 0, -1).size
        val crawlersCount = jedis.lrange(getCrawlersKey(host), 0, -1).size
        if(urlsCount > crawlersCount){
            jedis.set(getAvailabilityKey(host), "yes")
        } else{
            jedis.set(getAvailabilityKey(host), "no")
        }
    }

    private fun getCrawlersKey(host: String): String{
        return "$frontier:$host:crawlerIds"
    }

    private fun getURLsKey(host: String): String{
        return "$frontier:$host:urls"
    }

    private fun getAvailabilityKey(host: String): String{
        return "$frontier:$host:available"
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