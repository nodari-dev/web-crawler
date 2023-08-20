package redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

object RedisConnector {
    private val jedis = JedisPool("localhost", 6379)

    fun getJedis(): Jedis {
        return jedis.resource
    }
}