package storage.visitedurls

import interfaces.IURLHashStorage
import redis.RedisConnector
import storage.visitedurls.Configuration.DEFAULT_PATH
import storage.visitedurls.Configuration.VISITED_KEY
import storage.visitedurls.Configuration.VISITED_URLS_LIST_KEY
import java.util.concurrent.locks.ReentrantLock

object VisitedURLsStorage: IURLHashStorage{
    private val mutex = ReentrantLock()
    private val jedis = RedisConnector.getJedis()

    init {
        jedis.set(VISITED_KEY, VISITED_URLS_LIST_KEY)
    }

    override fun add(hash: Int){
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