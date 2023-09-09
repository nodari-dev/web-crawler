package storage

import redis.RedisManager

class TestUtils {
    private val jedis = RedisManager

    fun getDefaultPathContent(path: String): List<String> {
        return jedis.getListOfEntryKeys(path)
    }

    fun getDefaultPathChildContent(path: String, host: String): List<String>{
        return jedis.getListOfEntryKeys("$path:$host")
    }
}