package storage.hosts

import dto.HashedUrlPair
import interfaces.IHostsStorage
import redis.RedisConnector
import storage.RedisStorageUtils
import storage.hosts.Configuration.DEFAULT_PATH
import storage.hosts.Configuration.HOSTS_KEY
import storage.hosts.Configuration.HOSTS_LIST_KEY
import java.util.concurrent.locks.ReentrantLock

object HostsStorage: IHostsStorage {
    private val mutex = ReentrantLock()
    private val jedis = RedisConnector.getJedis()
    private val redisStorageUtils = RedisStorageUtils()
    private val robotsUtils = RobotsUtils()

    init {
        jedis.set(HOSTS_KEY, HOSTS_LIST_KEY)
    }

    override fun provideHost(host: String){
        if(isHostNew(host)){
            createHost(host)
        }
    }

    private fun createHost(host: String){
        mutex.lock()
        try{
            jedis.lpush(DEFAULT_PATH, host)
            setRobotsForHost(host)
        } finally {
            mutex.unlock()
        }
    }

    private fun setRobotsForHost(host: String){
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        val bannedURLs = robotsUtils.getDisallowedURLs(host)
        bannedURLs.forEach{formattedURL ->
            jedis.rpush(path, formattedURL.url)
        }
    }

    override fun isURLAllowed(host: String, url: String): Boolean{
        mutex.lock()
        try{
            if(isHostNew(host)){
                return true
            }

            val bannedURLs = getBannedURls(host)
            return bannedURLs.any{bannedURL -> !url.contains(bannedURL) }

        } finally {
            mutex.unlock()
        }
    }

    private fun isHostNew(host: String): Boolean{
        return jedis.lpos(DEFAULT_PATH, host) == null
    }

    private fun getBannedURls(host: String): List<String>{
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        return jedis.lrange(path, 0, -1)
    }
 }