package storage.interfaces

interface IHostsStorage {
    fun updateHost(host: String, urls: List<String>)
    fun isURLAllowed(host: String, url: String): Boolean
    fun doesHostExist(host: String): Boolean
}