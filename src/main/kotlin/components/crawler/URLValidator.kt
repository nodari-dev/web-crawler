package components.crawler

import components.storage.hosts.HostsStorage
import components.storage.url.URLStorage
import core.interfaces.components.IURLValidator

class URLValidator(
    override val hostsStorage: HostsStorage,
    override val urlStorage: URLStorage
) : IURLValidator {

    override fun canProcessURL(host: String, webLink: core.dto.WebLink?): Boolean {
        if (webLink == null) {
            return false
        }

        val isNew = urlStorage.doesNotExist(webLink.getHash())
        val isAllowed = hostsStorage.isURLAllowed(host, webLink.url)
        return isNew && isAllowed
    }
}