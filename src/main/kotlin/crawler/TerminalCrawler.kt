package crawler

import parser.Parser
import fetcher.Fetcher
import frontier.Frontier
import interfaces.ITerminalCrawler
import mu.KotlinLogging
import urlHashStorage.URLHashStorage
import utils.Utils
import java.net.URL

class TerminalCrawler(
    override val id: Int,
) : ITerminalCrawler, Thread() {
    private val fetcher = Fetcher()
    private val crawlerUtils = CrawlerUtils(this)

    private val counter = Counter
    private val utils = Utils
    val logger = KotlinLogging.logger("Crawler:${id}")
    var primaryHost: String? = null

    override fun run() {
        while (true) {
            if(crawlerUtils.canProceedCrawling()){
                val url = Frontier.getURL()
                url?.let{
                    counter.increment()
                    setPrimaryURL(url)
                    processPage(utils.formatURL(url))
                }
            } else{
                crawlerUtils.killCrawler()
            }

        }
    }

    private fun setPrimaryURL(url: String){
        primaryHost = URL(url).host
    }

    private fun processPage(url: String){
        logger.info ("#${counter.value} Fetched: $url")
        URLHashStorage.add(url.hashCode())
        val html = fetcher.getPageContent(url)

        html?.let{
            processChildURLs(html)
        }
    }

    private fun processChildURLs(html: String) {
        val urls = Parser.getURLs(html)
        urls.forEach { childURL ->
            val formattedURL = utils.formatURL(childURL)
            if (crawlerUtils.canProcessURL(formattedURL)) {
                send(formattedURL)
            }
        }
    }

    private fun send(url: String) {
        Frontier.addURL(url)
    }
}