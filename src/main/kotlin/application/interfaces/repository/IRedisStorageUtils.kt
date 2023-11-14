package application.interfaces.repository

interface IRedisStorageUtils {
    fun getEntryPath(defaultPath: String, key: String): String
}