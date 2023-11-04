package modules

import core.configuration.Illustrations
import mu.KLogger
import mu.KotlinLogging
import infrastructure.repository.RedisRepository
import application.parser.urlparser.URLParser
import storage.frontier.Frontier
import modules.interfaces.ISeedsManager

object SeedsManager: ISeedsManager {
    private val urlParser = URLParser()
    private var jedis = RedisRepository
    private var logger = KotlinLogging.logger("CrawlingManager")
    private var frontier = Frontier

    override fun startCrawling(seeds: List<String>){
        jedis.clear()
        if(seeds.isNotEmpty()){
            println(Illustrations.CrawlerStarted)
            seeds.forEach { seed ->
                val host = urlParser.getHostWithProtocol(seed)
                frontier.updateOrCreateQueue(host, core.dto.URLData(seed).url)
            }
        } else{
            logger.error("No seed urls provided")
        }
    }

    fun setup(loggerMock: KLogger, frontierMock: Frontier, jedisMock: RedisRepository){
        logger = loggerMock
        frontier = frontierMock
        jedis = jedisMock
    }
}