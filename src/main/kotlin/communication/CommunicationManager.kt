package communication

import crawler.CrawlersFactory
import dto.HashedUrlPair
import frontier.Frontier
import interfaces.ICommunicationManager
import parser.urlparser.URLParser
import redis.RedisConnector

object CommunicationManager: ICommunicationManager {
    private val frontier = Frontier
    private val crawlersFactory = CrawlersFactory
    private val urlParser = URLParser()
    private val startingPoints = mutableListOf<String>()
    private val jedis = RedisConnector.getJedis()

    /**
     * Sends starting points to frontier
     * Flushes all data from redis
     * @param seeds List of starting points
     */
    override fun startCrawling(seeds: List<String>){
        jedis.flushAll()
        startingPoints.forEach { seed ->
            val host = urlParser.getHostWithProtocol(seed)
            frontier.updateOrCreateQueue(host, HashedUrlPair(seed))
        }
    }

    /**
     * CrawlersFactory
     */
    override fun requestCrawlerTermination(crawler: Thread){
        crawlersFactory.killCrawler(crawler)
    }

    override fun requestCrawlerInitialization(host: String){
        crawlersFactory.createCrawler(host)
    }

    /**
     * Frontier
     */
    override fun requestURLFromFrontier(host: String): HashedUrlPair{
        return frontier.pullURL(host)
    }

    override fun isFrontierQueueEmpty(host: String): Boolean{
        return frontier.isQueueEmpty(host)
    }

    override fun sendURLToFrontierQueue(host: String, hashedUrlPair: HashedUrlPair){
        return frontier.updateOrCreateQueue(host, hashedUrlPair)
    }
}