package crawler

import configuration.Configuration

object CrawlersFactory {
    private val activeCrawlers = mutableListOf<Thread>()
    private val hostsToProcess = mutableListOf<String>()

    /**
     * Requests the initialization of a crawler for a specific host.
     * @param host The host to provide connection with frontier queue.
     */
    fun createCrawler(host: String) {
        hostsToProcess.add(host)
        generateNewCrawlers()
    }

    /**
     * Requests the termination of a crawler thread.
     * @param crawler The crawler thread to be terminated.
     */
    fun killCrawler(crawler: Thread) {
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