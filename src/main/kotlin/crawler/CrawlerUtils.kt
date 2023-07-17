package crawler

import crawlersManager.Configuration.CRAWLING_LIMIT
import dto.FormattedURL
import dto.URLRecord
import interfaces.ICrawlerUtils
import localStorage.HostsStorage
import localStorage.URLHashStorage

class CrawlerUtils : ICrawlerUtils {
    private val counter = Counter

    override fun canProcessURL(host: String, formattedURL: FormattedURL?): Boolean {
        if(formattedURL == null){
            return false
        }

        val urlRecord = URLRecord(formattedURL)
        val isNew = URLHashStorage.doesNotExist(urlRecord.getUniqueHash())
        val isAllowed = HostsStorage.isURLAllowed(host, formattedURL.value)
        return isNew && isAllowed
    }

    override fun canProceedCrawling(): Boolean {
        return counter.value != CRAWLING_LIMIT
    }
}