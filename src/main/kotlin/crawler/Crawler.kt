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
        while (true) {
            if(selectedHost != null){
                counter.increment()
                val urlRecord = frontier.pullURLRecord(selectedHost!!)
                if(urlRecord == null){
                    disconnectFromQueue()
                } else{
                    processPage(urlRecord)
                }
            }
            else {
                val host = frontier.pickFreeQueue()
                if(host != null){
                    counter.increment()
                    connectToQueueByHost(host)

                    frontier.pullURLRecord(selectedHost!!)?.let{urlRecord ->
                        processPage(urlRecord)
                    }
                }
                else{
                    this.interrupt()
                }
            }
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
//
//    private fun processHost(urlRecord: URLRecord){
//        val host = urlParser.getHostWithProtocol(urlRecord.getURL())
//        val url = urlRecord.getURL()
//        if (!hostStorage.isHostDefined(host)){
//            val bannedURLs = robots.getDisallowedURLs(host, url)
//            hostStorage.addHostRecord(host, bannedURLs)
//        }
//    }

    private fun processPage(urlRecord: URLRecord){
//        val isNotBanned = hostStorage.isURLBanned(selectedHost!!, urlRecord.getURL())
//        if(isNotBanned){
//
//        }

        URLHashStorage.add(urlRecord.getUniqueHash())
        val html = fetcher.getPageContent(urlRecord.getURL())

        html?.let{
            processChildURLs(html)
        }
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