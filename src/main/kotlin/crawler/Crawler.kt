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
import localStorage.VisitedURLs

class Crawler(
    override val id: Int,
    override val crawlerUtils: CrawlerUtils,
    override val fetcher: Fetcher,
    override val robots: Robots,
    override val hostConnector: HostConnector,
    override val dataAnalyzer: DataAnalyzer,
    override val urlParser: URLParser,
    override val frontier: Frontier,
    override val hostStorage: HostsStorage,
    override val urlHashStorage: VisitedURLs,
    override val kotlinLogging: KotlinLogging,
    override val counter: Counter
) : ICrawler, Thread() {
    private val logger = kotlinLogging.logger("Crawler:${id}")

    override fun run() {
        startCrawl()
    }

    private fun startCrawl(){
        while (true) {
            if(hostConnector.hasConnection()){
                processNewFrontierRecord()
            }
            else {
                processNewHost()
            }
        }
    }

    private fun processNewHost(){
        val host = frontier.pickFreeQueue()
        if(host != null){
            hostConnector.connect(host)
            processRobotsTxt()
            processNewFrontierRecord()
        }
    }

    private fun processNewFrontierRecord(){
        counter.increment()
        val host = hostConnector.host!!
        val urlRecord = frontier.pullURLRecord(host)
        if(urlRecord == null){
            hostConnector.disconnect()
            return
        }

        VisitedURLs.add(urlRecord.getUniqueHash())

        if(crawlerUtils.canProcessURL(host, urlRecord.formattedURL)){
            fetchHTML(urlRecord)
        }
    }

    private fun processRobotsTxt(){
        val host = hostConnector.host!!
        val disallowedURLs = robots.getDisallowedURLs(host)
        hostStorage.addHostRecord(host, disallowedURLs)
    }

    private fun fetchHTML(urlRecord: URLRecord){
        val html = fetcher.getPageContent(urlRecord.getURL())
        html?.let{
            val urls = urlParser.getURLs(html)
            if(!urlRecord.getURL().contains("robots.txt")){
                dataAnalyzer.getPageStats(html)
            }
            processFetchedURLs(urls)
        }
    }

    private fun processFetchedURLs(urls: List<FormattedURL>) {
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