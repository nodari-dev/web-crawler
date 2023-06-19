package crawler

import crawlersManager.Configuration.CRAWLING_LIMIT
import crawlersManager.Configuration.SINGLE_HOST_CRAWL
import interfaces.ICrawlerUtils
import urlHashStorage.URLHashStorage

class CrawlerUtils(override val crawler: TerminalCrawler) : ICrawlerUtils {
    private val counter = Counter

    override fun canProcessURL(url: String): Boolean {
        val isNew = !URLHashStorage.alreadyExists(url.hashCode())
        return if (SINGLE_HOST_CRAWL) {
            isNew && url.contains(crawler.primaryHost!!)

            } else {
                isNew
            }
    }

    override fun canProceedCrawling(): Boolean {
        return counter.value != CRAWLING_LIMIT
    }

    override fun killCrawler() {
        crawler.logger.info("I'm dead")
        crawler.interrupt()
    }
}