package redis

import interfaces.IRedisStorageUtils

class RedisStorageUtils: IRedisStorageUtils {
    override fun getEntryPath(defaultPath: String, key: String): String {
        return "$defaultPath:$key"
    }
}