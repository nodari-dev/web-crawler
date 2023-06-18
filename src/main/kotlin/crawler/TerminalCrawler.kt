package crawler

import parser.Parser
import dto.Page
import fetcher.Fetcher
import frontier.Frontier
import interfaces.ITerminalCrawler
import mu.KotlinLogging
import urlHashStorage.URLHashStorage
import java.net.URL

class TerminalCrawler(
    override val id: Int,
) : ITerminalCrawler, Thread() {
    private val crawlerUtils = CrawlerUtils(this)
    private val logger = KotlinLogging.logger("Crawler:${id}")
    private val fetcher = Fetcher()
    var primaryHost: String? = null

    override fun run() {
        while (true) {
            val url = Frontier.getURL()
            url?.let{
                primaryHost = URL(url).host
                Counter.increment()
                val page = Page(url)
                processPage(page)
            }
        }
    }

    private fun processPage(page: Page){
        logger.info ("#${Counter.show()} Fetched: ${page.url}")
        URLHashStorage.add(page.url.hashCode())
        val html = fetcher.getPageContent(page.url)

        html?.let{
            processChildURLs(html)
        }
    }

    private fun processChildURLs(html: String) {
        val urls = Parser.getURLs(html)
        urls.forEach { url ->
            if (crawlerUtils.canProcessURL(Page(url).url)) {
                send(Page(url).url)
            }
        }
    }

    private fun send(url: String) {
        Frontier.addURL(url)
    }

}