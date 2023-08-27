package communication

import crawler.CrawlersFactory
import dto.HashedUrlPair
import frontier.Frontier
import interfaces.ICommunicationManager
import mu.KotlinLogging
import parser.urlparser.URLParser
import redis.RedisConnector

object CommunicationManager: ICommunicationManager {
    private val frontier = Frontier
    private val crawlersFactory = CrawlersFactory
    private val jedis = RedisConnector.getJedis()
    private val urlParser = URLParser()
    private val logger = KotlinLogging.logger("CommunicationManager")

    /**
     * Sends starting points to frontier
     * Flushes all data from redis
     * @param seeds List of starting points
     */
    override fun startCrawling(seeds: List<String>){
        if(seeds.isNotEmpty()){
            jedis.flushAll()
            seeds.forEach { seed ->
                val host = urlParser.getHostWithProtocol(seed)
                frontier.updateOrCreateQueue(host, HashedUrlPair(seed))
            }
        } else{
            logger.error("Provide seed urls")
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