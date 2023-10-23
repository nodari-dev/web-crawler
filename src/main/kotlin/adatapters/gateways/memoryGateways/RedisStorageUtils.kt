package adatapters.gateways.memoryGateways

import core.interfaces.gateways.IRedisStorageUtils

class RedisStorageUtils: IRedisStorageUtils {
    override fun getEntryPath(defaultPath: String, key: String): String {
        return "$defaultPath:$key"
    }
}