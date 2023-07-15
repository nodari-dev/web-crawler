package crawler

import crawlersManager.Configuration.CRAWLING_LIMIT
import dto.FormattedURL
import dto.URLRecord
import interfaces.ICrawlerUtils
import localStorage.HostsStorage
import localStorage.URLHashStorage

class CrawlerUtils : ICrawlerUtils {
    private val counter = Counter

    override fun isURLValid(host: String, formattedURL: FormattedURL): Boolean {
        val urlRecord = URLRecord(formattedURL)
        val isNew = !URLHashStorage.alreadyExists(urlRecord.getUniqueHash())
        val isNotBanned = !HostsStorage.isURLBanned(host, formattedURL.value)
        return isNew && isNotBanned
    }

    override fun canProceedCrawling(): Boolean {
        return counter.value != CRAWLING_LIMIT
    }
}