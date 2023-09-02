package storage.url

import interfaces.IURLStorage
import redis.RedisConnector
import storage.url.Configuration.DEFAULT_PATH
import storage.url.Configuration.VISITED_KEY
import storage.url.Configuration.VISITED_URLS_LIST_KEY
import java.util.concurrent.locks.ReentrantLock

object URLStorage: IURLStorage{
    private val mutex = ReentrantLock()
    private val jedis = RedisConnector.getJedis()

    init {
        jedis.set(VISITED_KEY, VISITED_URLS_LIST_KEY)
    }

    override fun provideURL(hash: Int){
        mutex.lock()
        try{
            jedis.rpush(DEFAULT_PATH, hash.toString())
        } finally {
            mutex.unlock()
        }
    }

    override fun doesNotExist(hash: Int): Boolean{
        mutex.lock()
        try{
            return jedis.lpos(DEFAULT_PATH, hash.toString()) == null
        } finally {
            mutex.unlock()
        }
    }
}