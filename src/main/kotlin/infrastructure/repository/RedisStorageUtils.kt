package infrastructure.repository

import application.interfaces.memoryGateways.IRedisStorageUtils

class RedisStorageUtils: IRedisStorageUtils {
    override fun getEntryPath(defaultPath: String, key: String): String {
        return "$defaultPath:$key"
    }
}