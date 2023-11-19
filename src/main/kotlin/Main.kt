import application.parser.urlparser.URLParser
import core.dto.URLInfo
import infrastructure.repository.FrontierRepository
import infrastructure.repository.HostsRepository
import infrastructure.repository.VisitedURLsRepository
import modules.CrawlingManager
import mu.KotlinLogging
import redis.clients.jedis.JedisPool
import storage.frontier.Frontier
import storage.hosts.HostsStorage
import storage.url.VisitedURLs
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
    val hostsStorage = HostsStorage(hostsRepository)

    val urlInfo = URLInfo("https://ecospace.org.ua")
    val host = URLParser().getHostname(urlInfo.link)
    frontier.update(host, listOf(urlInfo))

    val crawlingManager = CrawlingManager(frontier, visitedURLs, hostsStorage)
    crawlingManager.run()
}