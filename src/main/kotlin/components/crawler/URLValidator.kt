package components.crawler

import components.storage.hosts.HostsStorage
import components.storage.url.URLStorage
import core.interfaces.components.IURLValidator

class URLValidator(
    override val hostsStorage: HostsStorage,
    override val urlStorage: URLStorage
) : IURLValidator {

    override fun canProcessURL(host: String, hashedUrlPair: core.dto.HashedURLPair?): Boolean {
        if (hashedUrlPair == null) {
            return false
        }

        val isNew = urlStorage.doesNotExist(hashedUrlPair.getHash())
        val isAllowed = hostsStorage.isURLAllowed(host, hashedUrlPair.url)
        return isNew && isAllowed
    }
}