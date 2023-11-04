package infrastructure.repository

import application.interfaces.memoryGateways.IMemoryRepository
import redis.clients.jedis.Jedis
import java.util.concurrent.locks.ReentrantLock

class RedisRepositoryV2(
    private val mutex: ReentrantLock,
    private val jedis: Jedis
    ): IMemoryRepository {

    override fun createEntry(path: String, key: String){
        mutex.lock()
        try{
            jedis.use { jedis ->
                jedis.lpush(path, key)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun updateEntry(path: String, key: String, value: String){
        mutex.lock()
        try{
            jedis.use { jedis ->
                jedis.rpush(getEntryPath(path, key), value)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun deleteEntry(path: String, key: String){
        mutex.lock()
        try{
            jedis.use{ jedis ->
                jedis.del(getEntryPath(path, key))
            }
        } finally {
            mutex.unlock()
        }

    }

    override fun getFirstEntryItem(path: String, key: String): String{
        mutex.lock()
        try{
            return jedis.use { jedis ->
                jedis.lpop(getEntryPath(path, key))
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun getListOfEntryKeys(path: String, key: String): List<String>{
        mutex.lock()
        try{
            return jedis.use { jedis ->
                jedis.lrange(getEntryPath(path, key), 0, -1)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun checkEntryEmptiness(path: String, key: String): Boolean{
        mutex.lock()
        try{
            return jedis.use { jedis ->
                jedis.lrange(getEntryPath(path, key), 0, -1).isEmpty()
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun isEntryKeyDefined(path: String, key: String): Boolean {
        mutex.lock()
        try{
            return jedis.use { jedis ->
                jedis.lpos(path, key) != null
            }
        } finally {
            mutex.unlock()
        }
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

    private fun getEntryPath(path: String, key: String): String {
        return "$path:$key"
    }
}