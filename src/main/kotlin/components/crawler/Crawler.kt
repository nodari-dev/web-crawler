package components.crawler

import components.dataExtractor.DataExtractor
import components.fetcher.Fetcher
import components.parser.urlparser.URLParser
import components.storage.frontier.Frontier
import components.storage.hosts.HostsStorage
import components.storage.url.URLStorage
import core.configuration.Configuration.SAVE_FILE_LOCATION
import core.dto.WebLink
import core.interfaces.components.ICrawler

import mu.KLogger

class Crawler(
    override val primaryHost: String,
    override var logger: KLogger,
    override val fetcher: Fetcher,
    override val urlValidator: URLValidator,
    override val urlParser: URLParser,
    override val crawlersFactory: CrawlersManager,
    override val dataExtractor: DataExtractor,
    override val hostsStorage: HostsStorage,
    override val urlStorage: URLStorage,
    override val frontier: Frontier,
) : ICrawler, Thread() {
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

    private fun deleteHostRelatedData() {
        hostsStorage.deleteHost(primaryHost)
        frontier.deleteQueue(primaryHost)
    }

    private fun processNewFrontierRecord() {
        val pulledURL = frontier.pullURL(primaryHost)
        if (urlValidator.canProcessURL(primaryHost, pulledURL)) {
            urlStorage.provideURL(pulledURL.getHash())
            hostsStorage.provideHost(primaryHost)
            processHTML(pulledURL)
        }
    }

    private fun processHTML(pulledURL: WebLink) {
        val html = fetcher.getPageHTML(pulledURL.url)
        html?.let {
            dataExtractor.extractSEODataToFile(html, pulledURL.url, SAVE_FILE_LOCATION)
            processChildURLs(urlParser.getURLs(html))
        }
    }

    private fun processChildURLs(urls: List<WebLink>) {
        val uniqueHashedUrlPairs = urls.toSet()
        uniqueHashedUrlPairs.forEach { hashedUrlPair ->
            val host = urlParser.getHostWithProtocol(hashedUrlPair.url)
            if (urlValidator.canProcessURL(host, hashedUrlPair)) {
                frontier.updateOrCreateQueue(host, hashedUrlPair.url)
            }
        }
    }
}