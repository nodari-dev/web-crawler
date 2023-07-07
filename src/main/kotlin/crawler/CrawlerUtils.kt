package crawler

import crawlersManager.Configuration.CRAWLING_LIMIT
import dto.HostWithProtocol
import dto.URLRecord
import interfaces.ICrawlerUtils
import localStorage.URLHashStorage

class CrawlerUtils : ICrawlerUtils {
    private val counter = Counter

    override fun isURLValid(url: URLRecord, hostWithProtocol: HostWithProtocol): Boolean {
        val isValid = !URLHashStorage.alreadyExists(url.hashCode())
        return isValid && url.toString().contains(hostWithProtocol.host)
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