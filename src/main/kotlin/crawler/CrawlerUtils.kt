package crawler

import crawlersManager.Configuration.CRAWLING_LIMIT
import dto.FormattedURL
import dto.URLRecord
import interfaces.ICrawlerUtils
import localStorage.URLHashStorage

class CrawlerUtils : ICrawlerUtils {
    private val counter = Counter

    override fun isURLNew(formattedURL: FormattedURL): Boolean {
        val urlRecord = URLRecord(formattedURL)
        return !URLHashStorage.alreadyExists(urlRecord.getUniqueHash())
    }

//    private fun isURLBanned(url: String): Boolean{
//        val host = URL(url).host
//        val bannedURLs = HostsStorage.get(host)?.bannedURLs
//
//        bannedURLs?.forEach { bannedURL ->
//            if (url.contains(bannedURL)) {
//                return true
//            }
//        }
//        return false
//    }

    override fun canProceedCrawling(): Boolean {
        return counter.value != CRAWLING_LIMIT
    }
}