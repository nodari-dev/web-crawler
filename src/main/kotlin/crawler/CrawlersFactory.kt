package crawler

import configuration.Configuration
import crawlingManager.CrawlingManager
import fetcher.Fetcher
import interfaces.ICrawlersFactory
import mu.KotlinLogging
import parser.urlparser.URLParser
import storage.frontier.Frontier
import storage.hosts.HostsStorage
import storage.url.URLStorage

object CrawlersFactory: ICrawlersFactory {
    private val activeCrawlers = mutableListOf<Thread>()
    private val hostsToProcess = mutableListOf<String>()

    override fun requestCrawlerInitialization(host: String) {
        hostsToProcess.add(host)
        generateNewCrawlers()
    }

    override fun removeTerminatedCrawler(crawler: Thread) {
        activeCrawlers.remove(crawler)
        if(hostsToProcess.isNotEmpty()){
            generateNewCrawlers()
        }
    }

    private fun generateNewCrawlers() {
        val hostsToRemove = mutableListOf<String>()
        for (i in 0 until hostsToProcess.size) {
            if (activeCrawlers.size < Configuration.MAX_NUMBER_OF_CRAWLERS) {
                val crawler = Crawler(
                    hostsToProcess[i],
                    KotlinLogging.logger("Crawler ${hostsToProcess[i]}"),
                    Fetcher(KotlinLogging.logger("Fetcher"),),
                    URLValidator(HostsStorage, URLStorage),
                    URLParser(),
                    CrawlersFactory,
                    CrawlingManager,
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