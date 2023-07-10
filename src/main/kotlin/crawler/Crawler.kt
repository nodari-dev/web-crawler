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
                frontier.pullURLRecord(selectedHost!!)?.let{urlRecord ->
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
                } else{
                    disconnectFromQueue()
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

    private fun processPage(urlRecord: URLRecord){
        URLHashStorage.add(urlRecord.getUniqueHash())

//        if(hostStorage.get(URL(url).host) == null){
//            val bannedURLs = robots.getDisallowedURLs(url)
//            hostStorage.add(primaryHost!!, bannedURLs)
//        }

        val html = fetcher.getPageContent(urlRecord.url)

        html?.let{
            processChildURLs(html)
        }
    }

    private fun processChildURLs(html: String) {
        val urls = urlParser.getURLs(html)
        val uniqueURLs = URLHashStorage.getNewURLs(urls.toMutableList())

        uniqueURLs.forEach{url ->
            if(crawlerUtils.isURLNew(URLRecord(url))){
                URLHashStorage.add(urls.hashCode())
                val host = urlParser.getHostWithProtocol(url)
                frontier.updateOrCreateQueue(host, url)
            }
        }
    }
}