package crawler

import configuration.Configuration

object CrawlersFactory {
    private val activeCrawlers = mutableListOf<Thread>()
    private val hostsToProcess = mutableListOf<String>()

    fun processQueue(host: String) {
        hostsToProcess.add(host)
        generateNewCrawlers()
    }

    fun killCrawler(crawler: Thread) {
        activeCrawlers.remove(crawler)
        generateNewCrawlers()
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