package crawler

import analyzer.DataAnalyzer
import dto.FormattedURL
import dto.URLRecord
import fetcher.Fetcher
import frontier.Frontier
import localStorage.HostsStorage
import interfaces.ICrawler
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots
import localStorage.URLHashStorage

class Crawler(
    override val id: Int,
    override val crawlerUtils: CrawlerUtils,
    override val fetcher: Fetcher,
    override val robots: Robots,
    override val dataAnalyzer: DataAnalyzer,
    override val urlParser: URLParser,
    override val frontier: Frontier,
    override val hostStorage: HostsStorage,
    override val urlHashStorage: URLHashStorage,
    override val kotlinLogging: KotlinLogging,
    override val counter: Counter
) : ICrawler, Thread() {
    private val logger = kotlinLogging.logger("Crawler:${id}")
    private var selectedHost: String? = null

    override fun run() {
        crawl()
    }

    private fun crawl(){
        while (true) {
            if(selectedHost == null){
                processNewHost()
            }
            else {
                processNewFrontierRecord()
            }
        }
    }

    private fun processNewHost(){
        val host = frontier.pickFreeQueue()
        if(host != null){
            connectToQueueByHost(host)
            processRobotsTxt()
            processNewFrontierRecord()
        }
    }

    private fun processNewFrontierRecord(){
        counter.increment()
        val urlRecord = frontier.pullURLRecord(selectedHost!!)
        if(urlRecord == null){
            disconnectFromQueue()
            return
        }

        if(crawlerUtils.canProcessURL(selectedHost!!, urlRecord.formattedURL)){
            fetchHTML(urlRecord)
        }
    }

    private fun processRobotsTxt(){
        val disallowedURLs = robots.getDisallowedURLs(selectedHost!!)
        hostStorage.addHostRecord(selectedHost!!, disallowedURLs)
    }

    private fun connectToQueueByHost(host: String){
        logger.info ("connected to queue with host: $host")
        selectedHost = host
    }

    private fun disconnectFromQueue(){
        logger.info ("disconnected from queue")
        selectedHost = null
    }

    private fun fetchHTML(urlRecord: URLRecord){
        URLHashStorage.add(urlRecord.getUniqueHash())

        val html = fetcher.getPageContent(urlRecord.getURL())
        html?.let{
            val urls = urlParser.getURLs(html)
            if(!urlRecord.getURL().contains("robots.txt")){
                dataAnalyzer.getPageStats(html)
            }
            processFoundURLs(urls)
        }
    }

    private fun processFoundURLs(urls: List<FormattedURL>) {
        val uniqueFormattedURLs = urls.toSet()
        uniqueFormattedURLs.forEach{formattedURL ->
            val host = urlParser.getHostWithProtocol(formattedURL.value)
            if(crawlerUtils.canProcessURL(host, formattedURL)){
                processURL(host, formattedURL)
            }
        }
    }

    private fun processURL(host: String, formattedURL: FormattedURL){
        frontier.updateOrCreateQueue(host, formattedURL)
    }
}