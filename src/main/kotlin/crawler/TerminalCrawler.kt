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

class TerminalCrawler(
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
    private var primaryHost: String? = null

    override fun run() {
        while (true) {
            if(primaryHost != null){
                counter.increment()
                frontier.pullURLRecord(primaryHost!!)?.let{urlRecord ->
                    processPage(urlRecord)
                }
            }
            else {
                val host = frontier.pickFreeQueue()
                host?.let{
                    counter.increment()
                    setPrimaryHost(host)

                    frontier.pullURLRecord(primaryHost!!)?.let{urlRecord ->
                        processPage(urlRecord)
                    }
                }
            }

        }
    }

    private fun setPrimaryHost(host: String){
        primaryHost = host
        logger.info ("connected to queue with host: $primaryHost")
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
        urls.filter { crawlerUtils.isURLNew(URLRecord(it))}
        val filteredURLs = urls.toSet().toMutableList()

        filteredURLs.forEach{url ->
            val host = urlParser.getHostWithProtocol(url)
            frontier.updateOrCreateQueue(host, url)
        }
    }
}