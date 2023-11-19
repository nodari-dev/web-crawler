import application.parser.urlparser.URLParser
import core.dto.URLInfo
import infrastructure.repository.FrontierRepository
import infrastructure.repository.HostsRepository
import infrastructure.repository.VisitedURLsRepository
import modules.CrawlingManager
import mu.KotlinLogging
import redis.clients.jedis.JedisPool
import storage.Frontier
import storage.HostsStorage
import storage.VisitedURLs
import java.util.concurrent.locks.ReentrantLock

fun main() {
    val reentrantLock = ReentrantLock()
    val jedis = JedisPool("localhost", 6379).resource

    val frontierRepository = FrontierRepository(reentrantLock, jedis)
    val frontierLogger = KotlinLogging.logger("Frontier")
    val frontier = Frontier(frontierRepository, frontierLogger)

    val visitedURLsRepository = VisitedURLsRepository(reentrantLock, jedis)
    val visitedURLs = VisitedURLs(visitedURLsRepository)

    val hostsRepository = HostsRepository(reentrantLock, jedis)
    val hostsLogger = KotlinLogging.logger("Hosts")
    val hostsStorage = HostsStorage(hostsRepository, hostsLogger)

    val urlInfo = URLInfo("https://ecospace.org.ua")
    val host = URLParser().getHostname(urlInfo.link)
    frontier.update(host, listOf(urlInfo))

    val crawlingManager = CrawlingManager(frontier, visitedURLs, hostsStorage)
    crawlingManager.run()
}