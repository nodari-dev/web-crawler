package core.interfaces.components

import components.storage.hosts.HostsStorage
import components.storage.url.URLStorage
import core.dto.WebLink

interface IURLValidator {
    val hostsStorage: HostsStorage
    val urlStorage: URLStorage
    fun canProcessURL(host: String, webLink: WebLink?): Boolean
}