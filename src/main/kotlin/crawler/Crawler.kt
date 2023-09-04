package crawler

import crawlingManager.CrawlingManager
import dto.HashedURLPair
import fetcher.Fetcher
import interfaces.ICrawler
import mu.KotlinLogging
import parser.urlparser.URLParser
import storage.frontier.Frontier
import storage.hosts.HostsStorage
import storage.url.URLStorage

class Crawler(
    override val primaryHost: String,
) : ICrawler, Thread() {
    private val logger = KotlinLogging.logger("Crawler $primaryHost")
    private val fetcher = Fetcher()
    private val urlValidator = URLValidator()
    private val urlParser = URLParser()
    private val crawlingManager = CrawlingManager
    private val crawlersFactory = CrawlersFactory
    private val hostsStorage = HostsStorage
    private val urlStorage = URLStorage
    private val frontier = Frontier
    private var canProceedCrawling = true

    override fun run() {
        logger.info("Started")
        while (canProceedCrawling) {
            communicateWithFrontier()
        }
    }

    private fun communicateWithFrontier() {
        if (frontier.isQueueEmpty(primaryHost)) {
            sendKillRequest()
        } else {
            processNewFrontierRecord()
        }
    }

    private fun sendKillRequest() {
        crawlersFactory.removeTerminatedCrawler(this)
        deleteHostRelatedData()
        canProceedCrawling = false
        logger.info("Stopped")
        return
    }

    private fun deleteHostRelatedData(){
        hostsStorage.deleteHost(primaryHost)
        frontier.deleteQueue(primaryHost)
    }

    private fun processNewFrontierRecord() {
        val pulledURL = frontier.pullURL(primaryHost)
        if (urlValidator.canProcessURL(primaryHost, pulledURL)) {
            urlStorage.provideURL(pulledURL.getHash())
            val html = fetcher.getPageHTML(pulledURL.url)
            processHTML(html, pulledURL)
        }
    }

    private fun processHTML(html: String?, pulledURL: HashedURLPair) {
        html?.let {
            crawlingManager.extractSEOData(html, pulledURL.url)
            processChildURLs(urlParser.getURLs(html))
        }
    }

    private fun processChildURLs(urls: List<HashedURLPair>) {
        val uniqueHashedUrlPairs = urls.toSet()
        uniqueHashedUrlPairs.forEach { hashedUrlPair ->
            val host = urlParser.getHostWithProtocol(hashedUrlPair.url)
            if (urlValidator.canProcessURL(host, hashedUrlPair)) {
                frontier.updateOrCreateQueue(host, hashedUrlPair)
            }
        }
    }
}