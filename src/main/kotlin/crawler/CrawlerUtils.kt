package crawler

import crawlersManager.Configuration.CRAWLING_LIMIT
import dto.URLRecord
import interfaces.ICrawlerUtils
import localStorage.URLHashStorage

class CrawlerUtils : ICrawlerUtils {
    private val counter = Counter

    override fun isURLValid(urlRecord: URLRecord, host: String): Boolean {
        val isValid = !URLHashStorage.alreadyExists(urlRecord.getUniqueHash())
        return isValid && urlRecord.url.contains(host)
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