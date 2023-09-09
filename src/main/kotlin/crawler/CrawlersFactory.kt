package crawler

import configuration.Configuration
import configuration.Configuration.MAX_NUMBER_OF_CRAWLERS
import dataExtractor.DataExtractor
import fetcher.Fetcher
import interfaces.ICrawlersFactory
import mu.KotlinLogging
import parser.urlparser.URLParser
import storage.frontier.Frontier
import storage.hosts.HostsStorage
import storage.url.URLStorage
import java.util.concurrent.locks.ReentrantLock

object CrawlersFactory: ICrawlersFactory {
    private val activeCrawlers = mutableListOf<Thread>()
    private val hostsToProcess = mutableListOf<String>()
    private var mutex = ReentrantLock()

    override fun requestCrawlerInitialization(host: String) {
        mutex.lock()
        try{
            hostsToProcess.add(host)
            generateNewCrawlers()
        } finally {
            mutex.unlock()
        }

    }

    override fun removeTerminatedCrawler(crawler: Thread) {
        mutex.lock()
        try{
            activeCrawlers.remove(crawler)
            if(hostsToProcess.isNotEmpty() && activeCrawlers.size != MAX_NUMBER_OF_CRAWLERS){
                generateNewCrawlers()
            }
        } finally {
            mutex.unlock()
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
                    CrawlersFactory,
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