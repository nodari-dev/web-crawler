package crawler

import crawlersManager.Configuration.CRAWLING_LIMIT
import crawlersManager.Configuration.SINGLE_HOST_CRAWL
import hostsStorage.HostsStorage
import interfaces.ICrawlerUtils
import parser.urlParser.URLParser
import urlHashStorage.URLHashStorage

class CrawlerUtils(override val crawler: TerminalCrawler) : ICrawlerUtils {
    private val counter = Counter
    private val urlParser = URLParser()

    override fun canProcessURL(url: String): Boolean {
        val isValid = !URLHashStorage.alreadyExists(url.hashCode()) && !isURLBanned(url)
        return if (SINGLE_HOST_CRAWL) {
            isValid && url.contains(crawler.primaryHost!!)
            } else {
                isValid
            }
    }

    private fun isURLBanned(url: String): Boolean{
        val bannedURLs = HostsStorage.get(urlParser.getHostname(url))?.bannedURLs

        if(bannedURLs != null) {
            bannedURLs.forEach { bannedURL ->
                if (url.contains(bannedURL)) {
                    return true
                }
            }
        }
        return false
    }

    override fun canProceedCrawling(): Boolean {
        return counter.value != CRAWLING_LIMIT
    }

    override fun killMe() {
        crawler.logger.info("I'm dead")
        crawler.interrupt()
    }
}