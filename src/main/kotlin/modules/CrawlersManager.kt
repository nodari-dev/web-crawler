package modules

import application.crawler.Crawler
import application.extractor.Extractor
import components.fetcher.Fetcher
import application.storage.frontier.Frontier
import application.storage.hosts.HostsStorage
import application.storage.mediator.StorageMediator
import application.storage.url.URLStorage
import application.validation.URLValidator
import core.configuration.Configuration.MAX_NUMBER_OF_CRAWLERS
import components.interfaces.ICrawlersManager
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
                    URLValidator(HostsStorage, URLStorage),
                    CrawlersManager,
                    storageMediator
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