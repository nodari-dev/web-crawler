package storage.hosts

import storage.interfaces.IHostsStorage

class HostsStorageV2(): IHostsStorage {
    //    private var jedis = RedisRepository
//    private var robotsUtils = RobotsUtils()
//
//    override fun provideHost(host: String){
//        if(isHostNew(host)){
//            createHost(host)
//        }
//    }
//
//    private fun createHost(host: String){
//        jedis.createEntry(DEFAULT_PATH, host)
//        setRobotsForHost(host)
//    }
//
//    override fun deleteHost(host: String){
//        jedis.deleteEntry(DEFAULT_PATH, host)
//    }
//
//    private fun setRobotsForHost(host: String){
//        val bannedURLs = robotsUtils.getDisallowedURLs(host)
//        bannedURLs.forEach{formattedURL ->
//            jedis.updateEntry(DEFAULT_PATH, host, formattedURL.url)
//        }
//    }
//
//    override fun isURLAllowed(host: String, url: String): Boolean{
//        if(isHostNew(host)){
//            return true
//        }
//
//        val bannedURLs = getBannedURls(host)
//        return bannedURLs.any{bannedURL -> !url.contains(bannedURL, true) }
//
//    }
//
//    private fun isHostNew(host: String): Boolean{
//        return !jedis.isEntryKeyDefined(DEFAULT_PATH, host)
//    }
//
//    private fun getBannedURls(host: String): List<String>{
//        return jedis.getListOfEntryKeys(DEFAULT_PATH, host)
//    }
//
//    fun setUpTest(mockRobotsUtils: RobotsUtils, mockJedis: RedisRepository){
//        robotsUtils = mockRobotsUtils
//        jedis = mockJedis
//    }
    override fun provideHost(host: String) {
        TODO("Not yet implemented")
    }

    override fun deleteHost(host: String) {
        TODO("Not yet implemented")
    }

    override fun isURLAllowed(host: String, url: String): Boolean {
        TODO("Not yet implemented")
    }
}