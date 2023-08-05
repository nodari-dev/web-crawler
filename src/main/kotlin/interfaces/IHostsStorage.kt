package interfaces

import dto.FormattedURL
import dto.HostRecord

interface IHostsStorage {
    fun addHostRecord(host: String, bannedURLs: List<FormattedURL>)
    fun isHostDefined(host: String): Boolean
    fun isURLAllowed(host: String, url: String): Boolean
}