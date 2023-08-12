package redisConnector

import redis.clients.jedis.JedisPooled

object RedisConnector {
    private val jedis = JedisPooled("localhost", 6379)

    fun getJedis(): JedisPooled {
        return jedis
    }
}