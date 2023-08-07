package crawler

import analyzer.DataAnalyzer
import controller.Controller
import dto.FormattedURL
import dto.URLRecord
import fetcher.Fetcher
import frontier.Frontier
import interfaces.ICrawler
import localStorage.HostsStorage
import localStorage.VisitedURLs
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

class Crawler(
    override val primaryHost: String,
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
    private val logger = kotlinLogging.logger("Crawler $primaryHost")
    private val communicationManager = Controller
    private var canProceedCrawling = true

    override fun run() {
        startCrawl()
    }

    private fun startCrawl(){
        logger.info ("Started")
        processRobotsTxt()
        while (canProceedCrawling) {
            processNewFrontierRecord()
        }
    }

    private fun processRobotsTxt(){
        val disallowedURLs = robots.getDisallowedURLs(primaryHost)
        hostStorage.addHostRecord(primaryHost, disallowedURLs)
    }

    private fun processNewFrontierRecord(){
        when(val urlRecord = frontier.pullURLRecord(primaryHost)){
            null -> sendMurderRequest()
            else -> {
                if(canProcessInternalURL(urlRecord)){
                    counter.increment()
                    VisitedURLs.add(urlRecord.getUniqueHash())
                    fetchHTML(urlRecord)
                }
            }
        }
    }

    private fun sendMurderRequest(){
        communicationManager.stopCrawler(this)
        canProceedCrawling = false
        logger.info ("Thank you, now I'm free... God bless your soul")
        return
    }

    private fun fetchHTML(urlRecord: URLRecord){
        val html = fetcher.getPageContent(urlRecord.getURL())
        html?.let{
            val urls = urlParser.getURLs(html)
            println(dataAnalyzer.getPageStats(html))
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
       return isURLValid(primaryHost, urlRecord.formattedURL)
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