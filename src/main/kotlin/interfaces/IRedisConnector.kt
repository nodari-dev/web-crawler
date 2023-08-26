package interfaces

import redis.clients.jedis.Jedis

interface IRedisConnector {
    fun getJedis(): Jedis
}