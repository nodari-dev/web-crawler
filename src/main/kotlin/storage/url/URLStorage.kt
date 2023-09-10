package storage.url

import interfaces.IURLStorage
import redis.RedisManager
import storage.url.Configuration.DEFAULT_PATH
import storage.url.Configuration.VISITED_KEY
import storage.url.Configuration.VISITED_URLS_LIST_KEY
import java.util.concurrent.locks.ReentrantLock

object URLStorage: IURLStorage{
    private var jedis = RedisManager

    init {
        jedis.createEntry(VISITED_KEY, VISITED_URLS_LIST_KEY)
    }

    override fun provideURL(hash: Int){
        jedis.updateEntry(DEFAULT_PATH, hash.toString())
    }

    override fun doesNotExist(hash: Int): Boolean{
        return !jedis.isEntryKeyDefined(DEFAULT_PATH, hash.toString())
    }

    fun setup(jedisMock: RedisManager){
        jedis = jedisMock
    }
}