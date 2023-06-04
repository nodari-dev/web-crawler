package crawler.terminalCrawler

import crawler.counter.Counter
import crawler.crawlerLogger.CrawlerLogger
import fetcher.Fetcher
import parser.Parser
import urlHashStorage.URLHashStorage
import dto.Page
import interfaces.ITerminalCrawler
import java.net.URL

class TerminalCrawler(
    override val startPage: Page,
    override val maxURLsToFetch: Int = 2,
    override val fetchOnlyProvidedHost: Boolean = false
) : ITerminalCrawler {
    private val storage = URLHashStorage()
    private val counter = Counter(0)

    private val queue: MutableList<Page> = mutableListOf(startPage)
    private val primaryHost: String = URL(startPage.url).host

    override fun start() {
        while (canProceedCrawling()) {
            val page: Page? = queue.removeFirstOrNull()
            if (page != null && isURLValid(page.url)) {
                processPage(page)
            }
        }
    }

    private fun canProceedCrawling(): Boolean {
        return queue.isNotEmpty() && counter.value != maxURLsToFetch
    }

    private fun isURLValid(url: String): Boolean {
        val isNew = !storage.values.contains(url.hashCode())
        return if (fetchOnlyProvidedHost) {
            isNew && url.contains(primaryHost)
        } else {
            isNew
        }
    }

    private fun processPage(page: Page) {
        storage.values.add(page.hash)
        counter.value++
        CrawlerLogger.fetched(counter, page.url)

        val html = Fetcher.getPageContent(page.url)
        if (html != null) {
            page.html = html
            processChildUrls(page)
        }

        queue.addAll(page.neighbors)
    }

    private fun processChildUrls(page: Page) {
        val urls = Parser.getFilteredURLs(page.html!!)
        urls.forEach { url ->
            if (isURLValid(url)) {
                val neighbor = Page(url)
                page.neighbors.add(neighbor)
                CrawlerLogger.found(counter, url)
            }
        }
    }
}