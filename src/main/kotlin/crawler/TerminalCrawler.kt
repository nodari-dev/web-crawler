package crawler

import dto.HostWithProtocol
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
    private var primaryHost: HostWithProtocol? = null

    override fun run() {
        while (true) {
            if(primaryHost != null){
                counter.increment()
                frontier.pullURLRecord(primaryHost!!.host)?.let{url ->
                    processPage(url)
                }
            }
            else {
                val host = frontier.pickFreeQueue()
                host?.let{
                    counter.increment()
                    setPrimaryHost(host)

                    frontier.pullURL(primaryHost!!.host)?.let{url ->
                        processPage(url)
                    }
                }
            }

        }
    }

    private fun setPrimaryHost(host: String){
        primaryHost = HostWithProtocol(host)
        logger.info ("connected to queue with host: $primaryHost")
    }

    private fun processPage(urlRecord: URLRecord){
        URLHashStorage.add(url.hashCode())

//        if(hostStorage.get(URL(url).host) == null){
//            val bannedURLs = robots.getDisallowedURLs(url)
//            hostStorage.add(primaryHost!!, bannedURLs)
//        }

        val html = fetcher.getPageContent(url)

        html?.let{
            processChildURLs(html)
        }
    }

    private fun processChildURLs(html: String) {
        val urls = urlParser.getURLs(html)
        urls.filter {  crawlerUtils.isURLValid(URLRecord(it), primaryHost!!)}
        val filteredURLs = urls.toSet().toMutableList()

        filteredURLs.forEach{url ->
            val urlRecord = URLRecord(url)
            frontier.updateOrCreateQueue(urlParser.getHostWithProtocol(urlRecord.url).host, urlRecord)
        }
    }
}