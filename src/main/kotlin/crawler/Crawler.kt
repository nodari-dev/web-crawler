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
    override val fetcher: Fetcher,
    override val robots: Robots,
    override val dataAnalyzer: DataAnalyzer,
    override val urlParser: URLParser,
    override val frontier: Frontier,
    override val hostStorage: HostsStorage,
    override val urlHashStorage: VisitedURLs,
    override val kotlinLogging: KotlinLogging,
    override val counter: Counter
) : ICrawler, Thread() {
    private val logger = kotlinLogging.logger("Crawler:${id}")
    private var selectedHost: String? = null

    override fun run() {
        startCrawl()
    }

    private fun startCrawl(){
        while (true) {
            when(hasConnection()){
                true -> processNewFrontierRecord()
                else -> processNewHost()
            }
        }
    }

    private fun hasConnection(): Boolean{
        return selectedHost != null
    }

    private fun processNewHost(){
        val host = frontier.pickFreeQueue()
        host?.let{
            connectToQueueByHost(host)
            processRobotsTxt()
            processNewFrontierRecord()
        }
    }

    private fun connectToQueueByHost(host: String){
        logger.info ("connected established with queue by host: $host")
        selectedHost = host
    }

    private fun processRobotsTxt(){
        val disallowedURLs = robots.getDisallowedURLs(selectedHost!!)
        hostStorage.addHostRecord(selectedHost!!, disallowedURLs)
    }

    private fun processNewFrontierRecord(){
        when(val urlRecord = frontier.pullURLRecord(selectedHost!!)){
            null -> disconnectFromQueue()
            else -> {
                if(canProcessInternalURL(urlRecord)) fetchHTML(urlRecord)
            }
        }
    }

    private fun disconnectFromQueue(){
        logger.info ("disconnected from queue")
        selectedHost = null
    }

    private fun fetchHTML(urlRecord: URLRecord){
        counter.increment()
        VisitedURLs.add(urlRecord.getUniqueHash())

        val html = fetcher.getPageContent(urlRecord.getURL())
        html?.let{
            val urls = urlParser.getURLs(html)
            dataAnalyzer.getPageStats(html)
            processFetchedURLs(urls)
        }
    }

    private fun processFetchedURLs(urls: List<FormattedURL>) {
        val uniqueFormattedURLs = urls.toSet()
        uniqueFormattedURLs.forEach{formattedURL ->
            val host = urlParser.getHostWithProtocol(formattedURL.value)
            if(canProcessExternalURL(host, formattedURL)){
                frontier.updateOrCreateQueue(host, formattedURL)
            }
        }
    }

    private fun canProcessInternalURL(urlRecord: URLRecord): Boolean{
       return isURLValid(selectedHost!!, urlRecord.formattedURL)
    }

    private fun canProcessExternalURL(host: String, formattedURL: FormattedURL?): Boolean{
        return isURLValid(host, formattedURL)
    }

    private fun isURLValid(host: String, formattedURL: FormattedURL?): Boolean{
        if(formattedURL == null){
            return false
        }

        val urlRecord = URLRecord(formattedURL)
        val isNew = VisitedURLs.doesNotExist(urlRecord.getUniqueHash())
        val isAllowed = HostsStorage.isURLAllowed(host, formattedURL.value)
        return isNew && isAllowed
    }
}