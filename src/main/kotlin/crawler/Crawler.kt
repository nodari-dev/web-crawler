package crawler

import communication.CommunicationManager
import dto.FormattedURL
import dto.FrontierRecord
import fetcher.Fetcher
import frontier.Frontier
import interfaces.ICrawler
import storage.hosts.HostsStorage
import storage.visitedurls.VisitedURLsStorage
import mu.KotlinLogging
import parser.urlparser.URLParser
import robots.RobotsManager

class Crawler(
    override val primaryHost: String,
    override val fetcher: Fetcher,
    override val robotsManager: RobotsManager,
    override val urlParser: URLParser,
    override val frontier: Frontier,
    override val hostsStorage: HostsStorage,
    override val visitedURLsStorage: VisitedURLsStorage,
    override val kotlinLogging: KotlinLogging,
    override val counter: Counter
) : ICrawler, Thread() {
    private val logger = kotlinLogging.logger("Crawler $primaryHost")
    private val communicationManager = CommunicationManager
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
        val disallowedURLs = robotsManager.getDisallowedURLs(primaryHost)
        hostsStorage.provideHost(primaryHost, disallowedURLs)
    }

    private fun processNewFrontierRecord(){
        val urlRecord = frontier.pullURL(primaryHost)
        if(urlRecord == null){
            sendMurderRequest()
        } else{
            if(canProcessInternalURL(urlRecord)){
                counter.increment()
                visitedURLsStorage.add(urlRecord.getUniqueHash())
                fetchHTML(urlRecord)
            }
        }
    }

    private fun sendMurderRequest(){
        communicationManager.stopCrawler(this)
        canProceedCrawling = false
        logger.info ("Stopped")
        return
    }

    private fun fetchHTML(frontierRecord: FrontierRecord){
        val url = frontierRecord.getURL()
        val html = fetcher.getPageContent(url)
        html?.let{
            val urls = urlParser.getURLs(html)
//            SEOStorage.updateOrCreateSEORecord(primaryHost, url, html)
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

    private fun canProcessInternalURL(frontierRecord: FrontierRecord): Boolean{
       return isURLValid(primaryHost, frontierRecord.formattedURL)
    }

    private fun canProcessExternalURL(host: String, formattedURL: FormattedURL?): Boolean{
        return isURLValid(host, formattedURL)
    }

    private fun isURLValid(host: String, formattedURL: FormattedURL?): Boolean{
        if(formattedURL == null){
            return false
        }

        val frontierRecord = FrontierRecord(formattedURL)
        val isNew = visitedURLsStorage.doesNotExist(frontierRecord.getUniqueHash())
        val isAllowed = hostsStorage.isURLAllowed(host, formattedURL.value)
        return isNew && isAllowed
    }
}