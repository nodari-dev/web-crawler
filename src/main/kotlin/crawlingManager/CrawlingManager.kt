package crawlingManager

import configuration.Illustrations
import dataExtractor.DataExtractor
import dto.HashedURLPair
import storage.frontier.Frontier
import interfaces.ICrawlingManager
import mu.KotlinLogging
import parser.urlparser.URLParser
import redis.RedisConnector

object CrawlingManager: ICrawlingManager {
    private val urlParser = URLParser()
    private val jedis = RedisConnector.getJedis()
    var logger = KotlinLogging.logger("CrawlingManager")
    var dataExtractor = DataExtractor()
    var frontier = Frontier

    override fun startCrawling(seeds: List<String>){
        jedis.flushAll()
        if(seeds.isNotEmpty()){
            println(Illustrations.CrawlerStarted)
            seeds.forEach { seed ->
                val host = urlParser.getHostWithProtocol(seed)
                frontier.updateOrCreateQueue(host, HashedURLPair(seed).url)
            }
        } else{
            logger.error("No seed urls provided")
        }
    }

    private fun setUpListeners(){
        frontier
    }
}