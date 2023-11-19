package storage.interfaces

import core.dto.URLInfo

interface IHostsStorage {
    fun updateHost(host: String, urlsInfo: List<URLInfo>)
    fun isURLAllowed(host: String, urlInfo: URLInfo): Boolean
    fun doesHostExist(host: String): Boolean
}