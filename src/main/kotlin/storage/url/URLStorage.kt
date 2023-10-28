package storage.url

import infrastructure.memoryGateways.RedisMemoryGateway
import storage.url.Configuration.DEFAULT_PATH
import storage.url.Configuration.PATH_KEY
import storage.interfaces.IURLStorage

object URLStorage: IURLStorage {
    private var jedis = RedisMemoryGateway

    override fun provideURL(hash: Int){
        jedis.updateEntry(DEFAULT_PATH, PATH_KEY, hash.toString())
    }

    override fun doesNotExist(hash: Int): Boolean{
        return !jedis.isEntryKeyDefined(DEFAULT_PATH, hash.toString())
    }

    fun setup(jedisMock: RedisMemoryGateway){
        jedis = jedisMock
    }
}