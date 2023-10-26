package modules

import core.configuration.Illustrations
import mu.KLogger
import mu.KotlinLogging
import infrastructure.memoryGateways.RedisMemoryGateway
import components.parser.urlparser.URLParser
import application.storage.frontier.Frontier
import components.interfaces.ICrawlingManager

object SeedsManager: ICrawlingManager {
    private val urlParser = URLParser()
    private var jedis = RedisMemoryGateway
    private var logger = KotlinLogging.logger("CrawlingManager")
    private var frontier = Frontier

    override fun startCrawling(seeds: List<String>){
        jedis.clear()
        if(seeds.isNotEmpty()){
            println(Illustrations.CrawlerStarted)
            seeds.forEach { seed ->
                val host = urlParser.getHostWithProtocol(seed)
                frontier.updateOrCreateQueue(host, core.dto.WebLink(seed).url)
            }
        } else{
            logger.error("No seed urls provided")
        }
    }

    fun setup(loggerMock: KLogger, frontierMock: Frontier, jedisMock: RedisMemoryGateway){
        logger = loggerMock
        frontier = frontierMock
        jedis = jedisMock
    }
}