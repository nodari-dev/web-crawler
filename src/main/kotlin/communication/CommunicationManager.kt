package communication

import configuration.Configuration.CONTINUE_FROM_CACHED_DATA
import crawler.CrawlersFactory
import dto.FormattedURL
import frontier.Frontier
import interfaces.ICommunicationManager
import storage.hosts.HostsStorage
import storage.visitedurls.VisitedURLsStorage
import parser.urlparser.URLParser
import redis.RedisConnector

object CommunicationManager: ICommunicationManager {
    private val frontier = Frontier
    private val crawlersFactory = CrawlersFactory
    private val urlParser = URLParser()
    private val startingPoints = mutableListOf<String>()
    private val jedis = RedisConnector.getJedis()
    private val visitedURLsStorage = VisitedURLsStorage
    private val hostsStorage = HostsStorage

    override fun start(){
        if(CONTINUE_FROM_CACHED_DATA){
            proceedWithExistingData()
        } else{
            startFromScratch()
        }
    }

    private fun proceedWithExistingData(){
        println("will be soon")
    }

    private fun startFromScratch(){
        jedis.flushAll()
        startingPoints.forEach { seed ->
            val host = urlParser.getHostWithProtocol(seed)
            frontier.updateOrCreateQueue(host, FormattedURL(seed))
        }
    }

    override fun addStartingPointURLs(seeds: List<String>) {
        startingPoints.addAll(seeds)
    }

    override fun requestCrawlerTermination(crawler: Thread){
        crawlersFactory.killCrawler(crawler)
    }

    override fun requestCrawlerInitialization(host: String){
        crawlersFactory.processQueue(host)
    }

    fun requestFrontierURL(host: String): FormattedURL{
        return frontier.pullURL(host)
    }

    fun checkFrontierQueueEmptiness(host: String): Boolean{
        return frontier.isQueueEmpty(host)
    }

    fun sendNewURLToFrontier(host: String, formattedURL: FormattedURL){
        return frontier.updateOrCreateQueue(host, formattedURL)
    }

    fun addVisitedURL(hash: Int){
        visitedURLsStorage.add(hash)
    }

    fun addHostData(host: String, bannedURLs: List<FormattedURL>){
        hostsStorage.provideHost(host, bannedURLs)
    }
}