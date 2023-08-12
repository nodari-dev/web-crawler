package communicationManager

import crawler.Counter
import crawler.Crawler
import dto.FormattedURL
import fetcher.Fetcher
import frontier.Frontier
import frontier.FrontierRedis
import interfaces.IController
import localStorage.HostsStorage
import localStorage.VisitedURLs
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

object CommunicationManager: IController {
    private val frontier = FrontierRedis
    private val urlParser = URLParser()
    private val activeCrawlers = mutableListOf<Thread>()
    private val hostsToProcess = mutableListOf<String>()
    private val startingPoints = mutableListOf<String>()

    override fun start(){
        startingPoints.forEach { seed ->
            val host = urlParser.getHostWithProtocol(seed)
            frontier.updateOrCreateQueue(host, FormattedURL(seed))
        }
    }

    override fun addStartingPointURLs(seeds: List<String>) {
        startingPoints.addAll(seeds)
    }

    override fun stopCrawler(crawler: Thread){
        activeCrawlers.remove(crawler)
        generateNewCrawlers()
    }

    override fun addHost(host: String){
        hostsToProcess.add(host)
        generateNewCrawlers()
    }

    private fun generateNewCrawlers(){
        val hostsToRemove = mutableListOf<String>()
        for(i in 0 until hostsToProcess.size){
            if(activeCrawlers.size < Configuration.MAX_NUMBER_OF_CRAWLERS){
                val crawler = Crawler(
                    hostsToProcess[i],
                    Fetcher(),
                    Robots(),
                    URLParser(),
                    FrontierRedis,
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
}