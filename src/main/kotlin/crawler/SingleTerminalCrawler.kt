package crawler

import dto.Page
import fetcher.Fetcher
import interfaces.ITerminalCrawler
import mu.KotlinLogging
import parser.Parser
import urlHashStorage.URLHashStorage
import java.net.URL

class SingleTerminalCrawler(
    override val startPage: Page,
    override val maxURLsToFetch: Int = 100,
    override val fetchOnlyProvidedHost: Boolean = true
) : ITerminalCrawler {
    private val storage = URLHashStorage()
    private val counter = Counter(0)

    private val queue: MutableList<Page> = mutableListOf(startPage)
    private val primaryHost: String = URL(startPage.url).host
    private val logger = KotlinLogging.logger("TerminalCrawler")

    override fun start() {
        while (canProceedCrawling()) {
            val page: Page? = queue.removeFirstOrNull()
            page?.let {
                if (isURLValid(page.url)) {
                    processPage(page)
                }
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
        logger.info { "#${counter.value} Processed: ${page.url}" }
        val html = Fetcher.getPageContent(page.url)
        html?.let {
            page.html = html
            processChildURLs(page)
        }

        queue.addAll(page.neighbors)
    }

    private fun processChildURLs(page: Page) {
        val urls = Parser.getFilteredURLs(page.html!!)
        urls.forEach { url ->
            if (isURLValid(url)) {
                val neighbor = Page(url)
                page.neighbors.add(neighbor)
                logger.info { "Found: $url" }
            }
        }
    }
}