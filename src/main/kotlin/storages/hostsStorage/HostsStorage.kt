package storages.hostsStorage

import dto.FormattedURL
import interfaces.IHostsStorage
import redisConnector.RedisConnector
import storages.hostsStorage.Configuration.DEFAULT_PATH
import storages.hostsStorage.Configuration.HOSTS_KEY
import storages.hostsStorage.Configuration.HOSTS_LIST_KEY
import java.util.concurrent.locks.ReentrantLock

object HostsStorage: IHostsStorage {
    private val mutex = ReentrantLock()
    private val jedis = RedisConnector.getJedis()

    init {
        jedis.set(HOSTS_KEY, HOSTS_LIST_KEY)
    }

    override fun provideHost(host: String, bannedURLs: List<FormattedURL>){
        if(isHostIsNew(host)){
            createHost(host, bannedURLs)
        }
    }

    private fun createHost(host: String, bannedURLs: List<FormattedURL>){
        mutex.lock()
        try{
            jedis.lpush(DEFAULT_PATH, host)
            bannedURLs.forEach{formattedURL ->
                jedis.rpush("${DEFAULT_PATH}:$host", formattedURL.value)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun isURLAllowed(host: String, url: String): Boolean{
        mutex.lock()
        try{
            if(isHostIsNew(host)){
                return true
            }

            val bannedURLs = getBannedURls(host)
            return bannedURLs.any{bannedURL -> !url.contains(bannedURL) }

        } finally {
            mutex.unlock()
        }
    }

    private fun isHostIsNew(host: String): Boolean{
        return jedis.lpos(DEFAULT_PATH, host) == null
    }

    private fun getBannedURls(host: String): List<String>{
        return jedis.lrange("$DEFAULT_PATH:$host", 0, -1)
    }
 }