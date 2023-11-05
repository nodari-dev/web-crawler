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

    override fun clear(){
        mutex.lock()
        try{
            jedis.flushAll()
            jedis.close()
        } finally {
            mutex.unlock()
        }
    }

    override fun create(host: String, list: List<String>) {
        mutex.lock()
        try{
            jedis.use { jedis ->
                list.forEach { item ->
                    jedis.rpush(host, item)
                }
                jedis.hset(table, field, host)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun getQueues(): String {
        mutex.lock()
        try{
            jedis.use { jedis ->
                return jedis.hget(table, field)
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

    override fun delete(host: String) {
        mutex.lock()
        try{
            jedis.use{ jedis ->
                jedis.hdel(table, field, host)
                jedis.del(host)
            }
        } finally {
            mutex.unlock()
        }
    }
}