package components.storage.url

import adatapters.gateways.memoryGateways.RedisMemoryGateway
import components.storage.url.Configuration.DEFAULT_PATH
import components.storage.url.Configuration.PATH_KEY
import core.interfaces.components.IURLStorage

object URLStorage: IURLStorage {
    private var jedis = RedisMemoryGateway

    override fun provideURL(hash: Int){
        jedis.updateEntry(DEFAULT_PATH, PATH_KEY ,hash.toString())
    }

    override fun doesNotExist(hash: Int): Boolean{
        return !jedis.isEntryKeyDefined(DEFAULT_PATH, hash.toString())
    }

    fun setup(jedisMock: RedisMemoryGateway){
        jedis = jedisMock
    }
}