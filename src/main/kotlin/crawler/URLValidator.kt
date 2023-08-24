package crawler

import dto.HashedUrlPair
import storage.hosts.HostsStorage
import storage.visitedurls.VisitedURLsStorage

class URLValidator {
    private val hostsStorage = HostsStorage
    private val visitedURLsStorage = VisitedURLsStorage

    fun canProcessInternalURL(host: String, hashedUrlPair: HashedUrlPair): Boolean{
        return isURLValid(host, hashedUrlPair)
    }

    fun canProcessExternalURL(host: String, hashedUrlPair: HashedUrlPair?): Boolean{
        return isURLValid(host, hashedUrlPair)
    }

    private fun isURLValid(host: String, hashedUrlPair: HashedUrlPair?): Boolean{
        if(hashedUrlPair == null){
            return false
        }

        val isNew = visitedURLsStorage.doesNotExist(hashedUrlPair.getHash())
        val isAllowed = hostsStorage.isURLAllowed(host, hashedUrlPair.value)
        return isNew && isAllowed
    }
}