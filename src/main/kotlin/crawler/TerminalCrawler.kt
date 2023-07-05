package crawler

import fetcher.Fetcher
import frontier.Frontier
import hostsStorage.HostsStorage
import interfaces.ITerminalCrawler
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots
import urlHashStorage.URLHashStorage
import utils.Utils
import java.net.URL

class TerminalCrawler(
    override val id: Int,
) : ITerminalCrawler, Thread() {
    private val fetcher = Fetcher()
    private val crawlerUtils = CrawlerUtils(this)
    private val robots = Robots()
    private val urlParser = URLParser()

    private val counter = Counter
    private val utils = Utils
    private val frontier = Frontier
    private val hostStorage = HostsStorage

    val logger = KotlinLogging.logger("Crawler:${id}")
    var primaryHost: String? = null

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
            if (crawlerUtils.canProcessURL(formattedURL)) {
                send(formattedURL)
            }
        }
    }

    private fun send(url: String) {
        frontier.addURL(url)
    }
}