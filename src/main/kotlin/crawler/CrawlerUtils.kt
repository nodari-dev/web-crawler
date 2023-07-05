package crawler

import crawlersManager.Configuration.CRAWLING_LIMIT
import crawlersManager.Configuration.SINGLE_HOST_CRAWL
import localStorage.HostsStorage
import interfaces.ICrawlerUtils
import parser.urlParser.URLParser
import localStorage.URLHashStorage
import java.net.URL

class CrawlerUtils() : ICrawlerUtils {
    private val counter = Counter

    override fun canProcessURL(url: String, host: String?): Boolean {
        val isValid = !URLHashStorage.alreadyExists(url.hashCode()) && !isURLBanned(url)
        return if (SINGLE_HOST_CRAWL) {
            isValid && url.contains(host!!)
            } else {
                isValid
            }
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