package crawler

import fetcher.Fetcher
import frontier.Frontier
import localStorage.HostsStorage
import interfaces.ITerminalCrawler
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots
import localStorage.URLHashStorage
import utils.Utils
import java.net.URL

class TerminalCrawler(
    override val id: Int,
    override val utils: Utils,
    override val crawlerUtils: CrawlerUtils,
    override val fetcher: Fetcher,
    override val robots: Robots,
    override val urlParser: URLParser,
    override val frontier: Frontier,
    override val hostStorage: HostsStorage,
    override val urlHashStorage: URLHashStorage,
    override val kotlinLogging: KotlinLogging,
    override val counter: Counter
) : ITerminalCrawler, Thread() {

    private val logger = kotlinLogging.logger("Crawler:${id}")
    private var primaryHost: String? = null

    override fun run() {
        while (true) {
            val url = frontier.getURL()
            url?.let{
                counter.increment()
                setPrimaryURL(url)
                processPage(utils.formatURL(url))
            }
        }
    }

    private fun setPrimaryURL(url: String){
        primaryHost = URL(url).host
    }

    private fun processPage(url: String){
        logger.info ("#${counter.value} Fetched: $url")
        URLHashStorage.add(url.hashCode())

        val bannedURLs = robots.getDisallowedURLs(url)
        hostStorage.add(url, bannedURLs)

        val html = fetcher.getPageContent(url)

        html?.let{
            processChildURLs(html)
        }
    }

    private fun processChildURLs(html: String) {
        val urls = urlParser.getURLs(html)
        urls.forEach { childURL ->
            val formattedURL = utils.formatURL(childURL)
            if (crawlerUtils.canProcessURL(formattedURL, primaryHost)) {
                send(formattedURL)
            }
        }
    }

    private fun send(url: String) {
        frontier.addURL(url)
    }
}