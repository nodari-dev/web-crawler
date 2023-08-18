package storage.seo

import analyzer.DataAnalyzer
import interfaces.ISEOStorage
import redis.RedisConnector
import storage.StorageUtils
import storage.seo.Configuration.DEFAULT_PATH
import storage.seo.Configuration.SEO_KEY
import storage.seo.Configuration.SEO_LIST_KEY
import java.util.concurrent.locks.ReentrantLock

object SEOStorage: ISEOStorage{
    private val dataAnalyzer = DataAnalyzer()
    private val mutex = ReentrantLock()
    private val storageUtils = StorageUtils()
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
        println("updateSEORecord $host")
        if(isSEODefinedForItem(host, url)){
            updateSEOItemDetails(host, url)
        } else{
            createNewSEOItem(host, url)
        }
    }

    private fun createNewSEOForHost(host: String, url: String, html: String){
        println("createSEORecord $host")
        jedis.lpush(DEFAULT_PATH, host)
        createNewSEOItem(host, url)
    }

    private fun isSEODefinedForItem(host: String, url: String): Boolean{
        val path = storageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        return jedis.lpos(path, url) != null
    }

    private fun updateSEOItemDetails(host: String, url: String){
        println("updateItem $host $url")
    }

    private fun createNewSEOItem(host: String, url: String){
        println("createItem $host $url")
        val path = storageUtils.getEntryPath(DEFAULT_PATH, listOf(host))
        jedis.rpush(path, url)
    }
}