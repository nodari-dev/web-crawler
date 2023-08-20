package crawler

import communication.CommunicationManager
import dto.FormattedURL
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
    override val urlValidator: URLValidator,
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
        logger.info("Started")
        processRobotsTxt()
        while (canProceedCrawling) {
            communicateWithFrontier()
        }
    }
    private fun processRobotsTxt() {
        val disallowedURLs = robotsManager.getDisallowedURLs(primaryHost)
        hostsStorage.provideHost(primaryHost, disallowedURLs)
    }

    private fun communicateWithFrontier() {
        if (frontier.isQueueEmpty(primaryHost)) {
            sendKillRequest()
        } else {
            processNewFrontierRecord()
        }
    }

    private fun sendKillRequest() {
        communicationManager.stopCrawler(this)
        canProceedCrawling = false
        logger.info("Stopped")
        return
    }

    private fun processNewFrontierRecord() {
        val pulledURL = frontier.pullURL(primaryHost)
        if (urlValidator.canProcessInternalURL(primaryHost, pulledURL)) {
            counter.increment()
            visitedURLsStorage.add(pulledURL.getHash())
            fetchHTML(pulledURL)
        }
    }

    private fun fetchHTML(formattedURL: FormattedURL) {
        val html = fetcher.getPageContent(formattedURL.value)
        html?.let {
            val urls = urlParser.getURLs(html)
//            SEOStorage.updateOrCreateSEORecord(primaryHost, url, html)
            processFetchedURLs(urls)
        }
    }

    private fun processFetchedURLs(urls: List<FormattedURL>) {
        val uniqueFormattedURLs = urls.toSet()
        uniqueFormattedURLs.forEach { formattedURL ->
            val host = urlParser.getHostWithProtocol(formattedURL.value)
            if (urlValidator.canProcessExternalURL(host, formattedURL)) {
                frontier.updateOrCreateQueue(host, formattedURL)
            }
        }
    }
}