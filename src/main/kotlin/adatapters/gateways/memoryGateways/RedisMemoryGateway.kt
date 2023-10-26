package adatapters.gateways.memoryGateways

import adatapters.interfaces.IMemoryGateway
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import java.util.concurrent.locks.ReentrantLock

object RedisMemoryGateway: IMemoryGateway {
    private var mutex = ReentrantLock()
    private var jedis = JedisPool("localhost", 6379).resource

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

    override fun deleteEntry(primaryPath: String, path: String, key: String){
        mutex.lock()
        try{
            jedis.use{ jedis ->
                jedis.del(path)
                jedis.lrem(primaryPath, 1 , key)
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
                jedis.lrange(path, 0, -1)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun checkEntryEmptiness(path: String, key: String): Boolean{
        mutex.lock()
        try{
            return jedis.use { jedis ->
                jedis.lrange(path, 0, -1).isEmpty()
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

    fun setupTest(mockJedis: Jedis, mockMutex: ReentrantLock){
        jedis = mockJedis
        mutex = mockMutex
    }
}