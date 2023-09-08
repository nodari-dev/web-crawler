package storage.hosts

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
    private var robotsUtils = RobotsUtils()

    init {
        jedis.set(HOSTS_KEY, HOSTS_LIST_KEY)
    }

    override fun provideHost(host: String){
        mutex.lock()
        try{
            if(isHostNew(host)){
                createHost(host)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun createHost(host: String){
        jedis.lpush(DEFAULT_PATH, host)
        setRobotsForHost(host)
    }

    override fun deleteHost(host: String){
        mutex.lock()
        try{
            jedis.del(redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host)))
            jedis.lrem(DEFAULT_PATH, 1 , host)
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
            return bannedURLs.any{bannedURL -> !url.contains(bannedURL, true) }

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

    fun setUpTest(mockRobotsUtils: RobotsUtils){
        robotsUtils = mockRobotsUtils
    }
 }