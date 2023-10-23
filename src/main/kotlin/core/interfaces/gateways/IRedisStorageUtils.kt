package core.interfaces.gateways

interface IRedisStorageUtils {
    fun getEntryPath(defaultPath: String, key: String): String
}