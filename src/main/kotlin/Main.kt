import application.parser.urlparser.URLParser
import core.dto.URLInfo
import infrastructure.repository.FrontierRepository
import infrastructure.repository.VisitedURLsRepository
import modules.CrawlingManager
import mu.KotlinLogging
import redis.clients.jedis.JedisPool
import storage.frontier.FrontierV2
import storage.url.VisitedURLs
import java.util.concurrent.locks.ReentrantLock

fun main() {
    val reentrantLock = ReentrantLock()
    val jedis = JedisPool("localhost", 6379).resource

    val frontierRepository = FrontierRepository(reentrantLock, jedis)
    val frontierLogger = KotlinLogging.logger("Frontier")
    val frontier = FrontierV2(frontierRepository, frontierLogger)

    val visitedURLsRepository = VisitedURLsRepository(reentrantLock, jedis)
    val visitedURLs = VisitedURLs(visitedURLsRepository)

    val urlInfo = URLInfo("https://ecospace.org.ua")
    val host = URLParser().getHostname(urlInfo.link)
    frontier.update(host, listOf(urlInfo))

    val crawlingManager = CrawlingManager(frontier, visitedURLs)
    crawlingManager.run()
}