package crawler

import crawlingManager.CrawlingManager
import dto.HashedUrlPair
import fetcher.Fetcher
import interfaces.ICrawler
import mu.KotlinLogging
import parser.urlparser.URLParser
import storage.visitedurls.VisitedURLsStorage

class Crawler(
    override val primaryHost: String,
) : ICrawler, Thread() {
    private val logger = KotlinLogging.logger("Crawler $primaryHost")
    private val fetcher = Fetcher()
    private val urlValidator = URLValidator()
    private val urlParser = URLParser()
    private val crawlingManager = CrawlingManager
    private var canProceedCrawling = true

    /**
     * Starts crawler as a Thread
     * Provides host data to hosts storage
     * Crawler will be alive until canProceedCrawling is true
     */
    override fun run() {
        logger.info("Started")
        while (canProceedCrawling) {
            communicateWithFrontier()
        }
    }

    private fun communicateWithFrontier() {
        if (crawlingManager.isFrontierQueueEmpty(primaryHost)) {
            sendKillRequest()
        } else {
            processNewFrontierRecord()
        }
    }

    private fun sendKillRequest() {
        crawlingManager.requestCrawlerTermination(this)
        canProceedCrawling = false
        logger.info("Stopped")
        return
    }

    private fun processNewFrontierRecord() {
        val pulledURL = crawlingManager.requestURLFromFrontier(primaryHost)
        if (urlValidator.canProcessInternalURL(primaryHost, pulledURL)) {
            Counter.increment()
            VisitedURLsStorage.add(pulledURL.getHash())
            fetchHTML(pulledURL)
        }
    }

    private fun fetchHTML(hashedUrlPair: HashedUrlPair) {
        val html = fetcher.getPageHTML(hashedUrlPair.url)
        html?.let {
            crawlingManager.extractSEOData(html, hashedUrlPair.url)
            processFetchedURLs(urlParser.getURLs(html))
        }
    }

    private fun processFetchedURLs(urls: List<HashedUrlPair>) {
        val uniqueHashedUrlPairs = urls.toSet()
        uniqueHashedUrlPairs.forEach { hashedUrlPair ->
            val host = urlParser.getHostWithProtocol(hashedUrlPair.url)
            if (urlValidator.canProcessExternalURL(host, hashedUrlPair)) {
                crawlingManager.sendURLToFrontierQueue(host, hashedUrlPair)
            }
        }
    }
}