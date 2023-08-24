package storage

import interfaces.IRedisStorageUtils

class RedisStorageUtils: IRedisStorageUtils {
    override fun getEntryPath(defaultPath: String, entriesList: List<String>): String {
        val list = entriesList.joinToString(separator = ":", prefix = ":")
        return "$defaultPath$list"
    }
}