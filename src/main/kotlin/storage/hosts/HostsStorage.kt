package storage.hosts

import dto.FormattedURL
import interfaces.IHostsStorage
import redis.RedisConnector
import storage.StorageUtils
import storage.hosts.Configuration.DEFAULT_PATH
import storage.hosts.Configuration.HOSTS_KEY
import storage.hosts.Configuration.HOSTS_LIST_KEY
import java.util.concurrent.locks.ReentrantLock

object HostsStorage: IHostsStorage {
    private val mutex = ReentrantLock()
    private val jedis = RedisConnector.getJedis()
    private val storageUtils = StorageUtils()

    init {
        jedis.set(HOSTS_KEY, HOSTS_LIST_KEY)
    }

    override fun provideHost(host: String, bannedURLs: List<FormattedURL>){
        if(isHostNew(host)){
            createHost(host, bannedURLs)
        }
    }

    private fun createHost(host: String, bannedURLs: List<FormattedURL>){
        mutex.lock()
        try{
            jedis.lpush(DEFAULT_PATH, host)
            val path = storageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
            bannedURLs.forEach{formattedURL ->
                jedis.rpush(path, formattedURL.value)
            }
        } finally {
            mutex.unlock()
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
        val path = storageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        return jedis.lrange(path, 0, -1)
    }
 }