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
) : ICrawler, Thread() {
    private val logger = KotlinLogging.logger("Crawler $primaryHost")
    private val fetcher = Fetcher()
    private val robotsManager = RobotsManager()
    private val urlValidator = URLValidator()
    private val urlParser = URLParser()
    private val communicationManager = CommunicationManager
    private val counter = Counter
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
        communicationManager.addHostData(primaryHost, disallowedURLs)
    }

    private fun communicateWithFrontier() {
        if (communicationManager.checkFrontierQueueEmptiness(primaryHost)) {
            sendKillRequest()
        } else {
            processNewFrontierRecord()
        }
    }

    private fun sendKillRequest() {
        communicationManager.requestCrawlerTermination(this)
        canProceedCrawling = false
        logger.info("Stopped")
        return
    }

    private fun processNewFrontierRecord() {
        val pulledURL = communicationManager.requestFrontierURL(primaryHost)
        if (urlValidator.canProcessInternalURL(primaryHost, pulledURL)) {
            counter.increment()
            communicationManager.addVisitedURL(pulledURL.getHash())
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
                communicationManager.sendNewURLToFrontier(host, formattedURL)
            }
        }
    }
}