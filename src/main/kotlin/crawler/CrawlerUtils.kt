package crawler

import crawlersManager.Configuration
import interfaces.ICrawlerUtils
import urlHashStorage.URLHashStorage

class CrawlerUtils(override val crawler: TerminalCrawler) : ICrawlerUtils {
    override fun canProcessURL(url: String): Boolean {
        val isNew = !URLHashStorage.alreadyExists(url.hashCode())
        return if (Configuration.SINGLE_HOST_CRAWL) {
            isNew && url.contains(crawler.primaryHost!!)

            } else {
                isNew
            }
    }
}