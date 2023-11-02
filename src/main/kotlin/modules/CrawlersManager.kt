package modules

import application.crawler.Crawler
import application.extractor.Extractor
import application.fetcher.Fetcher
import storage.frontier.Frontier
import storage.hosts.HostsStorage
import storage.mediator.StorageMediator
import storage.url.URLStorage
import core.configuration.Configuration.MAX_NUMBER_OF_CRAWLERS
import modules.interfaces.ICrawlersManager
import application.parser.urlparser.URLParser
import mu.KotlinLogging

object CrawlersManager: ICrawlersManager {
    private val activeCrawlers = mutableListOf<Thread>()
    private val hostsToProcess = mutableListOf<String>()

    override fun requestCrawlerInitialization(host: String) {
        hostsToProcess.add(host)
        generateNewCrawlers()
    }

    override fun removeTerminatedCrawler(crawler: Thread) {
        activeCrawlers.remove(crawler)
        if(hostsToProcess.isNotEmpty() && activeCrawlers.size != MAX_NUMBER_OF_CRAWLERS){
            generateNewCrawlers()
        }
    }

    private fun generateNewCrawlers() {
        val storageMediator = StorageMediator(Frontier, HostsStorage, URLStorage)
        val hostsToRemove = mutableListOf<String>()
        for (i in 0 until hostsToProcess.size) {
            if (activeCrawlers.size < MAX_NUMBER_OF_CRAWLERS) {
                val crawler = Crawler(
                    hostsToProcess[i],
                    KotlinLogging.logger("Crawler ${hostsToProcess[i]}"),
                    Fetcher(KotlinLogging.logger("Fetcher")),
                    CrawlersManager,
                    storageMediator,
                    URLParser(),
                    Extractor()
                )
                activeCrawlers.add(crawler)
                crawler.start()
                hostsToRemove.add(hostsToProcess[i])
            }
        }
        hostsToProcess.removeAll(hostsToRemove)
        hostsToRemove.clear()
    }
}