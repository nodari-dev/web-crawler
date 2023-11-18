package infrastructure.repository

import infrastructure.repository.interfaces.IRedisStorageUtils

class RedisStorageUtils: IRedisStorageUtils {
    override fun getEntryPath(defaultPath: String, key: String): String {
        return "$defaultPath:$key"
    }
}