package crawler

import crawlersManager.Configuration
import interfaces.ICrawlerUtils

class CrawlerUtils(override val crawler: TerminalCrawler) : ICrawlerUtils {
    override fun canProcessURL(url: String): Boolean {
            val isNew = !crawler.urlHashStorage.alreadyExists(url.hashCode())
//            return if (Configuration.SINGLE_HOST_CRAWL) {
////            isNew && url.contains(primaryHost)
//                isNew
//
//            } else {
//                isNew
//            }

        return isNew

    }
}