package infrastructure.repository

import core.dto.URLInfo
import infrastructure.repository.interfaces.IVisitedURLsRepository
import redis.clients.jedis.Jedis
import java.util.concurrent.locks.ReentrantLock

class VisitedURLsRepository(
    private val mutex: ReentrantLock,
    private val jedis: Jedis
): IVisitedURLsRepository {

    private val visitedURLs = "visitedURLs"

    override fun getOnlyNewURLs(urlInfoList: List<URLInfo>): List<URLInfo> {
        mutex.lock()
        try{
            jedis.use { jedis ->
                return urlInfoList.filter { urlInfo -> jedis.lpos(visitedURLs, urlInfo.hash) == null }
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun update(urlInfo: URLInfo) {
        mutex.lock()
        try{
            jedis.use{ jedis ->
                jedis.rpush(visitedURLs, urlInfo.hash)
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun isNew(url: String): Boolean {
        mutex.lock()
        try{
            jedis.use{ jedis ->
                return jedis.lpos(visitedURLs, url)  == null
            }
        } finally {
            mutex.unlock()
        }
    }

    override fun clear(){
        mutex.lock()
        try{
            jedis.flushAll()
            jedis.close()
        } finally {
            mutex.unlock()
        }
    }
}
