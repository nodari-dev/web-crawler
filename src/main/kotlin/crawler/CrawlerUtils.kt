package crawler

import dto.FormattedURL
import dto.URLRecord
import interfaces.ICrawlerUtils
import localStorage.HostsStorage
import localStorage.VisitedURLs

class CrawlerUtils : ICrawlerUtils {
    override fun canProcessURL(host: String, formattedURL: FormattedURL?): Boolean {
        if(formattedURL == null){
            return false
        }

        val urlRecord = URLRecord(formattedURL)
        val isNew = VisitedURLs.doesNotExist(urlRecord.getUniqueHash())
        val isAllowed = HostsStorage.isURLAllowed(host, formattedURL.value)
        return isNew && isAllowed
    }
}