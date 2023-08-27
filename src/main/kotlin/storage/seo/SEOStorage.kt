package storage.seo

import analyzer.SEODataAnalyzer
import interfaces.ISEOStorage
import redis.RedisConnector
import storage.RedisStorageUtils
import storage.seo.Configuration.DEFAULT_PATH
import storage.seo.Configuration.SEO_KEY
import storage.seo.Configuration.SEO_LIST_KEY
import java.util.concurrent.locks.ReentrantLock

object SEOStorage: ISEOStorage{
    private val SEODataAnalyzer = SEODataAnalyzer()
    private val mutex = ReentrantLock()
    private val redisStorageUtils = RedisStorageUtils()
    private val jedis = RedisConnector.getJedis()

    init {
        jedis.set(SEO_KEY, SEO_LIST_KEY)
    }

    override fun updateOrCreateSEOEntry(host: String, url: String, html: String) {
        mutex.lock()
        try{
            if(isSEODefinedForHost(host)){
                updateSEODetails(host, url ,html)
            } else{
                createNewSEOForHost(host, url, html)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun isSEODefinedForHost(host: String): Boolean{
        return jedis.lpos(DEFAULT_PATH, host) != null
    }

    private fun updateSEODetails(host: String, url: String, html: String){
        if(isSEODefinedForItem(host, url)){
            updateSEOItemDetails(host, url)
        } else{
            createNewSEOItem(host, url)
        }
    }

    private fun createNewSEOForHost(host: String, url: String, html: String){
        jedis.lpush(DEFAULT_PATH, host)
        createNewSEOItem(host, url)
    }

    private fun isSEODefinedForItem(host: String, url: String): Boolean{
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        return jedis.lpos(path, url) != null
    }

    private fun updateSEOItemDetails(host: String, url: String){
        println("updateItem $host $url")
    }

    private fun createNewSEOItem(host: String, url: String){
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        jedis.rpush(path, url)
    }
}