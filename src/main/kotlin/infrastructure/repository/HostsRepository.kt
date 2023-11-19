package infrastructure.repository

import infrastructure.repository.interfaces.IHostsRepository
import redis.clients.jedis.Jedis
import java.util.concurrent.locks.ReentrantLock

class HostsRepository(
    private val mutex: ReentrantLock,
    private val jedis: Jedis
): IHostsRepository {

    private val hostsInfo = "hosts-info"

    override fun update(host: String, urls: List<String>) {
        mutex.lock()
        try{
            jedis.use { jedis ->
                urls.forEach{url ->
                    jedis.rpush(getHostsKey(host), url)
                }
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun get(host: String): MutableList<String> {
        mutex.lock()
        try{
            jedis.use { jedis ->
                return jedis.lrange(getHostsKey(host), 0 ,  -1)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun getHostsKey(host: String): String{
        return "$hostsInfo:$host"
    }
}