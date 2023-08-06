package crawlersManager

import analyzer.DataAnalyzer
import crawler.Counter
import crawler.Crawler
import crawlersManager.Configuration.MAX_NUMBER_OF_CRAWLERS
import fetcher.Fetcher
import frontier.Frontier
import interfaces.ICrawlersManager
import localStorage.HostsStorage
import localStorage.VisitedURLs
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

object CrawlersManager : ICrawlersManager {
    private val activeCrawlers = mutableListOf<Thread>()
    private val hostsToProcess = mutableListOf<String>()

    fun provideNewHost(host: String){
        hostsToProcess.add(host)
        generateNewCrawlers()
    }

    fun murder(crawler: Thread){
        activeCrawlers.remove(crawler)
    }

    private fun generateNewCrawlers(){
        val hostsToRemove = mutableListOf<String>()
        for(i in 0 until hostsToProcess.size){
            if(activeCrawlers.size < MAX_NUMBER_OF_CRAWLERS){
                val crawler = Crawler(
                    hostsToProcess[i],
                    Fetcher(),
                    Robots(),
                    DataAnalyzer(),
                    URLParser(),
                    Frontier,
                    HostsStorage,
                    VisitedURLs,
                    KotlinLogging,
                    Counter
                )
                activeCrawlers.add(crawler)
                crawler.start()
                hostsToRemove.add(hostsToProcess[i])
            }
        }
        hostsToProcess.removeAll(hostsToRemove)
        hostsToRemove.clear()
    }

    private fun joinThreads() {
        activeCrawlers.forEach { thread ->
            thread.join()
        }
    }
}