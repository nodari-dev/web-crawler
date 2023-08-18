package interfaces

interface IStorageUtils {
    fun getEntryPath(defaultPath: String, entriesList: List<String>): String
}