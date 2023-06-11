package crawler

import dataAnalyzer.parser.Parser
import dto.Page
import fetcher.Fetcher
import frontier.Frontier
import interfaces.ITerminalCrawler
import mu.KotlinLogging
import urlHashStorage.URLHashStorage
import java.net.URL

class TerminalCrawler(
    override val id: Int,
    override val frontier: Frontier,
    override val urlHashStorage: URLHashStorage,
    override val counter: Counter

) : ITerminalCrawler, Thread() {
    // Handles crawler actions
    // 1. Skip url
    // 2. Save data
    // 3. validate content
    // 4. handles logic
    private var primaryHost: String? = null
    private val crawlerUtils = CrawlerUtils(this)
    private val logger = KotlinLogging.logger("Crawler:${id}}")

    override fun run() {
        while (true) {
            val url = frontier.getURL()
            if (url != null) {
                primaryHost = URL(url).host
                // if(isValidUrl(url)){

                counter.increment()

                logger.info{"#${counter.show()} processed: $url"}
                val html = Fetcher.getPageContent(url)
                html?.let{
//                    page.html = html
                    processChildURLs(html)
                }
//                send(url)
            }
        }
    }

    private fun processChildURLs(html: String) {
        val urls = Parser.getURLs(html)
        urls.forEach { url ->
            if (crawlerUtils.canProcessURL(url)) {
                logger.info { "Found: $url" }
                send(url)
            }
        }
    }

    private fun send(url: String) {
        // imitation sending new urls to frontier
        frontier.addURL(url)
    }


    override fun updateStorage(hash: Int) {
//        urlHashStorage.values.add(hash)
    }

}