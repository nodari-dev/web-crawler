package application.interfaces

import application.storage.hosts.HostsStorage
import application.storage.url.URLStorage
import core.dto.WebLink

interface IURLValidator {
    val hostsStorage: HostsStorage
    val urlStorage: URLStorage
    fun canProcessURL(host: String, webLink: WebLink?): Boolean
}