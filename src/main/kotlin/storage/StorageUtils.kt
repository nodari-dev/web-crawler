package storage

import interfaces.IStorageUtils

class StorageUtils: IStorageUtils {
    override fun getEntryPath(defaultPath: String, entriesList: List<String>): String {
        val list = entriesList.joinToString(separator = ":", prefix = ":")
        return "$defaultPath$list"
    }
}