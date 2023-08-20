package communication

import configuration.Configuration
import configuration.Configuration.CONTINUE_FROM_CACHED_DATA
import crawler.Counter
import crawler.Crawler
import crawler.URLValidator
import dto.FormattedURL
import fetcher.Fetcher
import frontier.Frontier
import interfaces.IController
import storage.hosts.HostsStorage
import storage.visitedurls.VisitedURLsStorage
import mu.KotlinLogging
import parser.urlparser.URLParser
import redis.RedisConnector
import robots.RobotsManager

object CommunicationManager: IController {
    private val frontier = Frontier
    private val urlParser = URLParser()
    private val activeCrawlers = mutableListOf<Thread>()
    private val hostsToProcess = mutableListOf<String>()
    private val startingPoints = mutableListOf<String>()
    private val jedis = RedisConnector.getJedis()

    override fun start(){
        if(CONTINUE_FROM_CACHED_DATA){

        } else{
            jedis.flushAll()
        }

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

    override fun notifyWithNewQueue(host: String){
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
                    RobotsManager(),
                    URLParser(),
                    URLValidator(),
                    Frontier,
                    HostsStorage,
                    VisitedURLsStorage,
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