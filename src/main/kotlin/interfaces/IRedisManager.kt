package interfaces


interface IRedisManager {
    fun createEntry(path: String, key: String)
    fun updateEntry(path: String, key: String)
    fun deleteEntry(primaryPath: String, path: String, key: String)
    fun getFirstEntryItem(path: String): String
    fun getListOfEntryKeys(path: String): List<String>
    fun checkEntryEmptiness(path: String): Boolean
    fun isEntryKeyDefined(path: String, key: String): Boolean
    fun clear()
}