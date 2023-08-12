package storages.visitedURLsStorage

import interfaces.IURLHashStorage
import redisConnector.RedisConnector
import storages.visitedURLsStorage.Configuration.DEFAULT_PATH
import storages.visitedURLsStorage.Configuration.VISITED_KEY
import storages.visitedURLsStorage.Configuration.VISITED_URLS_LIST_KEY
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