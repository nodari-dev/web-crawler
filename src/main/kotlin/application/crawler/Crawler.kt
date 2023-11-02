package application.crawler

import core.dto.WebLink
import application.interfaces.ICrawler
import application.interfaces.IDataExtractor
import modules.interfaces.ICrawlersManager
import application.interfaces.IFetcher
import application.interfaces.IURLParser
import core.configuration.Configuration
import application.interfaces.IMediator
import storage.mediator.Actions.*
import core.dto.WebPage

import mu.KLogger


class Crawler(
    private val primaryHost: String,
    private var logger: KLogger,
    private val fetcher: IFetcher,
    private val crawlersManager: ICrawlersManager,
    private val storageMediator: IMediator,
    private val urlParser: IURLParser,
    private val extractor: IDataExtractor
) : ICrawler, Thread() {
    private var canProceedCrawling = true

    override fun run() {
        logger.info("Started")
        while (canProceedCrawling) {
            communicateWithFrontier()
        }
    }

    private fun communicateWithFrontier() {
        if (storageMediator.request(FRONTIER_IS_QUEUE_EMPTY, primaryHost)) {
            sendKillRequest()
        } else {
            processNewFrontierRecord()
        }
    }

    private fun sendKillRequest() {
        crawlersManager.removeTerminatedCrawler(this)
        deleteHostRelatedData()
        canProceedCrawling = false
        logger.info("Stopped")
        return
    }

    private fun deleteHostRelatedData() {
        storageMediator.request<Unit>(HOSTS_DELETE, primaryHost)
        storageMediator.request<Unit>(FRONTIER_DELETE_QUEUE, primaryHost)
    }

    private fun processNewFrontierRecord() {
        val pulledURL = storageMediator.request<WebLink>(FRONTIER_PULL, primaryHost)
        if (canProcessURL(primaryHost, pulledURL)) {
            storageMediator.request<Unit>(URLS_UPDATE, pulledURL.getHash())
            storageMediator.request<Unit>(HOSTS_PROVIDE_NEW, primaryHost)
            processURL(pulledURL)
        }
    }

    private fun processURL(pulledURL: WebLink) {
        val html = fetcher.getPageHTML(pulledURL.url)
        html?.let {
            val webPage = WebPage(pulledURL, html)
            processWebPage(webPage)
        }
    }

    private fun processWebPage(webPage: WebPage) {
        if(webPage.html == null){
            println("html is not correct")
        } else{
            extractor.extractSEODataToFile(webPage.html, webPage.link.url, Configuration.SAVE_FILE_LOCATION)
            processChildURLs(urlParser.getURLs(webPage.html))
        }
    }

    private fun processChildURLs(urls: List<WebLink>) {
        val uniqueHashedUrlPairs = urls.toSet()
        uniqueHashedUrlPairs.forEach { hashedUrlPair ->
            val host = urlParser.getHostWithProtocol(hashedUrlPair.url)
            if (canProcessURL(host, hashedUrlPair)) {
                storageMediator.request<Unit>(FRONTIER_UPDATE, host, hashedUrlPair.url)
            }
        }
    }

    private fun canProcessURL(host: String, webLink: core.dto.WebLink?): Boolean {
        if (webLink == null) {
            return false
        }

        val isNew = storageMediator.request<Boolean>(URLS_CHECK_EXISTENCE, webLink.getHash())
        val isAllowed = storageMediator.request<Boolean>(HOSTS_IS_URL_ALLOWED, host, webLink.url)
        return isNew && isAllowed
    }
}