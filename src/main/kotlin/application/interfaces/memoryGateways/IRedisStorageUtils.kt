package application.interfaces.memoryGateways

interface IRedisStorageUtils {
    fun getEntryPath(defaultPath: String, key: String): String
}