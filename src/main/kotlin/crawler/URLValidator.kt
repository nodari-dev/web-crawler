package crawler

import dto.FormattedURL
import storage.hosts.HostsStorage
import storage.visitedurls.VisitedURLsStorage

class URLValidator {
    private val hostsStorage = HostsStorage
    private val visitedURLsStorage = VisitedURLsStorage

    fun canProcessInternalURL(host: String, formattedURL: FormattedURL): Boolean{
        return isURLValid(host, formattedURL)
    }

    fun canProcessExternalURL(host: String, formattedURL: FormattedURL?): Boolean{
        return isURLValid(host, formattedURL)
    }

    private fun isURLValid(host: String, formattedURL: FormattedURL?): Boolean{
        if(formattedURL == null){
            return false
        }

        val isNew = visitedURLsStorage.doesNotExist(formattedURL.getHash())
        val isAllowed = hostsStorage.isURLAllowed(host, formattedURL.value)
        return isNew && isAllowed
    }
}