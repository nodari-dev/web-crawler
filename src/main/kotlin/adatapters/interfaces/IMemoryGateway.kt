package adatapters.interfaces


interface IMemoryGateway {
    fun createEntry(path: String, key: String)
    fun updateEntry(path: String, key: String, value: String)
    fun deleteEntry(primaryPath: String, path: String, key: String)
    fun getFirstEntryItem(path: String, key: String): String
    fun getListOfEntryKeys(path: String, key: String): List<String>
    fun checkEntryEmptiness(path: String, key: String): Boolean
    fun isEntryKeyDefined(path: String, key: String): Boolean
    fun clear()
}