package interfaces

import dto.FormattedURL
import dto.HostRecord

interface IHostsStorage {
    fun addHostRecord(host: String, bannedURLs: List<FormattedURL>)
    fun getHostRecord(host: String): HostRecord?
    fun isHostDefined(host: String): Boolean
    fun isURLAllowed(host: String, url: String): Boolean
}