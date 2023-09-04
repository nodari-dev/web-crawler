package crawler

import dto.HashedURLPair
import interfaces.IURLValidator
import storage.hosts.HostsStorage
import storage.url.URLStorage

class URLValidator: IURLValidator {
    private val hostsStorage = HostsStorage
    private val visitedURLsStorage = URLStorage

    /**
     * Checks if URL can be processed by crawler
     * @param host to call hostsStorage
     * @param hashedUrlPair the actual URL with hash to check if it was visited or was banned by robots.txt
     */
    override fun canProcessURL(host: String, hashedUrlPair: HashedURLPair?): Boolean{
        if(hashedUrlPair == null){
            return false
        }

        val isNew = visitedURLsStorage.doesNotExist(hashedUrlPair.getHash())
        val isAllowed = hostsStorage.isURLAllowed(host, hashedUrlPair.url)
        return isNew && isAllowed
    }
}