package application.validation

import application.storage.hosts.HostsStorage
import application.storage.url.URLStorage
import application.interfaces.IURLValidator

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