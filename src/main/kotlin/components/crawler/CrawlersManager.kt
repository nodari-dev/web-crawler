package components.crawler

import components.dataExtractor.DataExtractor
import components.fetcher.Fetcher
import components.parser.urlparser.URLParser
import components.storage.frontier.Frontier
import components.storage.hosts.HostsStorage
import components.storage.url.URLStorage
import core.configuration.Configuration.MAX_NUMBER_OF_CRAWLERS
import core.interfaces.components.ICrawlersManager
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
        val hostsToRemove = mutableListOf<String>()
        for (i in 0 until hostsToProcess.size) {
            if (activeCrawlers.size < MAX_NUMBER_OF_CRAWLERS) {
                val crawler = Crawler(
                    hostsToProcess[i],
                    KotlinLogging.logger("Crawler ${hostsToProcess[i]}"),
                    Fetcher(KotlinLogging.logger("Fetcher")),
                    URLValidator(HostsStorage, URLStorage),
                    URLParser(),
                    CrawlersManager,
                    DataExtractor(),
                    HostsStorage,
                    URLStorage,
                    Frontier
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