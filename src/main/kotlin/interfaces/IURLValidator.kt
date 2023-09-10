package interfaces

import dto.HashedURLPair
import storage.hosts.HostsStorage
import storage.url.URLStorage

interface IURLValidator {
    val hostsStorage: HostsStorage
    val urlStorage: URLStorage
    fun canProcessURL(host: String, hashedUrlPair: HashedURLPair?): Boolean
}