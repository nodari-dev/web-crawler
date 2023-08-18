package storage

import interfaces.IStorageUtils

class StorageUtilsTest: IStorageUtils {
    override fun getEntryPath(defaultPath: String, entriesList: List<String>): String {
        return "$defaultPath:${entriesList.joinToString( ":")}"
    }
}