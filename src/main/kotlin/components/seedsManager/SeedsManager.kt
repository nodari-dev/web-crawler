package components.seedsManager

import core.configuration.Illustrations
import mu.KLogger
import mu.KotlinLogging
import adatapters.gateways.memoryGateways.RedisMemoryGateway
import components.parser.urlparser.URLParser
import components.storage.frontier.Frontier
import core.interfaces.components.ICrawlingManager

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
                frontier.updateOrCreateQueue(host, core.dto.HashedURLPair(seed).url)
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