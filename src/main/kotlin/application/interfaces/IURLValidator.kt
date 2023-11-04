package application.interfaces

import storage.hosts.HostsStorage
import storage.url.URLStorage
import core.dto.URLData

interface IURLValidator {
    val hostsStorage: HostsStorage
    val urlStorage: URLStorage
    fun canProcessURL(host: String, URLData: URLData?): Boolean
}