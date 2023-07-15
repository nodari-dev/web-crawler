package crawler

import dto.URLRecord
import fetcher.Fetcher
import frontier.Frontier
import localStorage.HostsStorage
import interfaces.ITerminalCrawler
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots
import localStorage.URLHashStorage

class Crawler(
    override val id: Int,
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
    private var selectedHost: String? = null

    override fun run() {
        crawl()
    }

    private fun crawl(){
        while (true) {
            if(selectedHost != null){
                pullURLFromFrontier()
            }
            else {
                val host = frontier.pickFreeQueue()
                if(host != null){
                    connectToQueueByHost(host)
                    pullURLFromFrontier()
                }
            }
        }
    }

    private fun pullURLFromFrontier(){
        counter.increment()
        val urlRecord = frontier.pullURLRecord(selectedHost!!)
        if(urlRecord == null){
            disconnectFromQueue()
        } else{
            processPage(urlRecord)
        }
    }

    private fun connectToQueueByHost(host: String){
        logger.info ("connected to queue with host: $host")
        selectedHost = host
    }

    private fun disconnectFromQueue(){
        logger.info ("disconnected from queue")
        selectedHost = null
    }

    private fun processPage(urlRecord: URLRecord){
        URLHashStorage.add(urlRecord.getUniqueHash())

        val html = fetcher.getPageContent(urlRecord.getURL())
        html?.let{ processChildURLs(html) }
    }

    private fun processChildURLs(html: String) {
        val urls = urlParser.getURLs(html)
        val uniqueFormattedURLs = urls.toSet()

        uniqueFormattedURLs.forEach{formattedURL ->
            val host = urlParser.getHostWithProtocol(formattedURL.value)
            if(crawlerUtils.isURLValid(host, formattedURL)){
                URLHashStorage.add(formattedURL.value.hashCode())
                frontier.updateOrCreateQueue(host, formattedURL.value)
            }
        }
    }
}