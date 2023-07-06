package crawler

import crawlersManager.Configuration.CRAWLING_LIMIT
import localStorage.HostsStorage
import interfaces.ICrawlerUtils
import localStorage.URLHashStorage
import java.net.URL

class CrawlerUtils() : ICrawlerUtils {
    private val counter = Counter

    override fun isURLValid(url: String, host: String): Boolean {
        val isValid = !URLHashStorage.alreadyExists(url.hashCode())
        return isValid && url.contains(host)
    }

    private fun isURLBanned(url: String): Boolean{
        val host = URL(url).host
        val bannedURLs = HostsStorage.get(host)?.bannedURLs

        bannedURLs?.forEach { bannedURL ->
            if (url.contains(bannedURL)) {
                return true
            }
        }
        return false
    }

    override fun canProceedCrawling(): Boolean {
        return counter.value != CRAWLING_LIMIT
    }
}