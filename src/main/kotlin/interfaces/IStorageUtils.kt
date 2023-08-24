package interfaces

interface IRedisStorageUtils {
    fun getEntryPath(defaultPath: String, entriesList: List<String>): String
}