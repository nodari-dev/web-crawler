package seedsManager

import configuration.Illustrations
import dto.HashedURLPair
import storage.frontier.Frontier
import interfaces.ICrawlingManager
import mu.KLogger
import mu.KotlinLogging
import parser.urlparser.URLParser
import redis.RedisManager

object SeedsManager: ICrawlingManager {
    private val urlParser = URLParser()
    private var jedis = RedisManager
    private var logger = KotlinLogging.logger("CrawlingManager")
    private var frontier = Frontier

    override fun startCrawling(seeds: List<String>){
        jedis.clear()
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

    fun setup(loggerMock: KLogger, frontierMock: Frontier, jedisMock: RedisManager){
        logger = loggerMock
        frontier = frontierMock
        jedis = jedisMock
    }
}