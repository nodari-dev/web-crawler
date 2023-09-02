package crawler

import configuration.Configuration
import interfaces.ICrawlersFactory

object CrawlersFactory: ICrawlersFactory {
    private val activeCrawlers = mutableListOf<Thread>()
    private val hostsToProcess = mutableListOf<String>()

    /**
     * Requests the initialization of a crawler for a specific host.
     * @param host The host to provide connection with frontier queue.
     */
    override fun requestCrawlerInitialization(host: String) {
        hostsToProcess.add(host)
        generateNewCrawlers()
    }

    /**
     * Removes terminated crawler from list of active crawlers
     * Requests initialization of new to proceed with new hosts
     * @param crawler The crawler thread to be terminated.
     */
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