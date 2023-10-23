package components.storage.hosts

import adatapters.gateways.memoryGateways.RedisMemoryGateway
import adatapters.gateways.memoryGateways.RedisStorageUtils
import components.storage.hosts.Configuration.DEFAULT_PATH
import components.storage.hosts.Configuration.HOSTS_KEY
import components.storage.hosts.Configuration.HOSTS_LIST_KEY
import core.interfaces.components.IHostsStorage

object HostsStorage: IHostsStorage {
    private val redisStorageUtils = RedisStorageUtils()
    private var jedis = RedisMemoryGateway
    private var robotsUtils = RobotsUtils()

    init {
        jedis.createEntry(HOSTS_KEY, HOSTS_LIST_KEY)
    }

    override fun provideHost(host: String){
        if(isHostNew(host)){
            createHost(host)
        }
    }

    private fun createHost(host: String){
        jedis.createEntry(DEFAULT_PATH, host)
        setRobotsForHost(host)
    }

    override fun deleteHost(host: String){
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, host)
        jedis.deleteEntry(DEFAULT_PATH, path, host)
    }

    private fun setRobotsForHost(host: String){
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, host)
        val bannedURLs = robotsUtils.getDisallowedURLs(host)
        bannedURLs.forEach{formattedURL ->
            jedis.updateEntry(path, formattedURL.url)
        }
    }

    override fun isURLAllowed(host: String, url: String): Boolean{
        if(isHostNew(host)){
            return true
        }

        val bannedURLs = getBannedURls(host)
        return bannedURLs.any{bannedURL -> !url.contains(bannedURL, true) }

    }

    private fun isHostNew(host: String): Boolean{
        return !jedis.isEntryKeyDefined(DEFAULT_PATH, host)
    }

    private fun getBannedURls(host: String): List<String>{
        val path = redisStorageUtils.getEntryPath(DEFAULT_PATH, host)
        return jedis.getListOfEntryKeys(path)

    }

    fun setUpTest(mockRobotsUtils: RobotsUtils, mockJedis: RedisMemoryGateway){
        robotsUtils = mockRobotsUtils
        jedis = mockJedis
    }
 }