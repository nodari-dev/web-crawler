package redis

import interfaces.IRedisConnector
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool

object RedisConnector: IRedisConnector {
    private val jedis = JedisPool("localhost", 6379)

    override fun getJedis(): Jedis {
        return jedis.resource
    }
}