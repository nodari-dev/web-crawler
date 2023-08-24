package crawler

import communication.CommunicationManager
import dto.HashedUrlPair
import fetcher.Fetcher
import interfaces.ICrawler
import mu.KotlinLogging
import parser.urlparser.URLParser
import robots.RobotsUtils

class Crawler(
    override val primaryHost: String,
) : ICrawler, Thread() {
    private val logger = KotlinLogging.logger("Crawler $primaryHost")
    private val fetcher = Fetcher()
    private val robotsUtils = RobotsUtils()
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
        val disallowedURLs = robotsUtils.getDisallowedURLs(primaryHost)
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

    private fun fetchHTML(hashedUrlPair: HashedUrlPair) {
        val html = fetcher.getPageContent(hashedUrlPair.value)
        html?.let {
            val urls = urlParser.getURLs(html)
//            SEOStorage.updateOrCreateSEORecord(primaryHost, url, html)
            processFetchedURLs(urls)
        }
    }

    private fun processFetchedURLs(urls: List<HashedUrlPair>) {
        val uniqueFormattedURLs = urls.toSet()
        uniqueFormattedURLs.forEach { formattedURL ->
            val host = urlParser.getHostWithProtocol(formattedURL.value)
            if (urlValidator.canProcessExternalURL(host, formattedURL)) {
                communicationManager.sendNewURLToFrontier(host, formattedURL)
            }
        }
    }
}