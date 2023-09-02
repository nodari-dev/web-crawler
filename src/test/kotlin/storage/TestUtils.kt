package storage

import redis.RedisConnector

class TestUtils {
    private val jedis = RedisConnector.getJedis()

    fun getDefaultPathContent(path: String): MutableList<String>? {
        return jedis.lrange(path ,0 , -1)
    }

    fun getDefaultPathChildContent(path: String, host: String): MutableList<String>? {
        return jedis.lrange("$path:$host" ,0 , -1)
    }
}