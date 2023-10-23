package components.storage.url

import adatapters.gateways.memoryGateways.RedisMemoryGateway
import components.storage.url.Configuration.DEFAULT_PATH
import components.storage.url.Configuration.VISITED_KEY
import components.storage.url.Configuration.VISITED_URLS_LIST_KEY
import core.interfaces.components.IURLStorage

object URLStorage: IURLStorage {
    private var jedis = RedisMemoryGateway

    init {
        jedis.createEntry(VISITED_KEY, VISITED_URLS_LIST_KEY)
    }

    override fun provideURL(hash: Int){
        jedis.updateEntry(DEFAULT_PATH, hash.toString())
    }

    override fun doesNotExist(hash: Int): Boolean{
        return !jedis.isEntryKeyDefined(DEFAULT_PATH, hash.toString())
    }

    fun setup(jedisMock: RedisMemoryGateway){
        jedis = jedisMock
    }
}