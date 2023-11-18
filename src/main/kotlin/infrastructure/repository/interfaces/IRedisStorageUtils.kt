package infrastructure.repository.interfaces

interface IRedisStorageUtils {
    fun getEntryPath(defaultPath: String, key: String): String
}