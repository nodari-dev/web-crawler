package application.crawler

import application.extractor.Extractor
import components.fetcher.Fetcher
import storage.frontier.Frontier
import storage.hosts.HostsStorage
import storage.url.URLStorage
import modules.CrawlersManager
import application.validation.URLValidator
import core.dto.WebLink
import application.interfaces.ICrawler
import components.contentProcessor.ContentProcessor
import core.dto.WebPage

import mu.KLogger

class Crawler(
    private val primaryHost: String,
    private var logger: KLogger,
    private val fetcher: Fetcher,
    private val urlValidator: URLValidator,
    private val crawlersFactory: CrawlersManager,
    private val hostsStorage: HostsStorage,
    private val urlStorage: URLStorage,
    private val frontier: Frontier,
) : ICrawler, Thread() {
    private var canProceedCrawling = true
    private val contentProcessor = ContentProcessor(Extractor(), frontier, urlValidator)

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
        CrawlersManager.removeTerminatedCrawler(this)
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
            processURL(pulledURL)
        }
    }

    private fun processURL(pulledURL: WebLink) {
        val html = fetcher.getPageHTML(pulledURL.url)
        html?.let {
            val webPage = WebPage(pulledURL, html)
            contentProcessor.processWebPage(webPage)
        }
    }


}