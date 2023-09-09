package storage.url

import interfaces.IURLStorage
import redis.RedisManager
import storage.url.Configuration.DEFAULT_PATH
import storage.url.Configuration.VISITED_KEY
import storage.url.Configuration.VISITED_URLS_LIST_KEY
import java.util.concurrent.locks.ReentrantLock

object URLStorage: IURLStorage{
    private val mutex = ReentrantLock()
    private val jedis = RedisManager

    init {
        jedis.createEntry(VISITED_KEY, VISITED_URLS_LIST_KEY)
    }

    override fun provideURL(hash: Int){
//        mutex.lock()
//        try{
//            jedis.updateEntry(DEFAULT_PATH, hash.toString())
//        } finally {
//            mutex.unlock()
//        }
        jedis.updateEntry(DEFAULT_PATH, hash.toString())
    }

    override fun doesNotExist(hash: Int): Boolean{
//        mutex.lock()
//        try{
//            return !jedis.isEntryKeyDefined(DEFAULT_PATH, hash.toString())
//        } finally {
//            mutex.unlock()
//        }
        return !jedis.isEntryKeyDefined(DEFAULT_PATH, hash.toString())
    }
}