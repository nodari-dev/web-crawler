import application.parser.urlparser.URLParser
import configuration.Configuration
import core.dto.URLInfo
import infrastructure.repository.FrontierRepository
import infrastructure.repository.HostsRepository
import infrastructure.repository.VisitedURLsRepository
import operators.CrawlingOperator
import mu.KotlinLogging
import operators.ApplicationStartupOperator
import redis.clients.jedis.JedisPool
import storage.Frontier
import storage.HostsStorage
import storage.VisitedURLs
import java.util.concurrent.locks.ReentrantLock

fun main(args: Array<String>) {
    ApplicationStartupOperator().setupArgs(args)

    val lock = ReentrantLock()
    val jedis = JedisPool(Configuration.HOST, Configuration.PORT).resource

    val frontierRepository = FrontierRepository(lock, jedis)
    val frontierLogger = KotlinLogging.logger("Frontier")
    val frontier = Frontier(frontierRepository, frontierLogger)

    val visitedURLsRepository = VisitedURLsRepository(lock, jedis)
    val visitedURLs = VisitedURLs(visitedURLsRepository)

    val hostsRepository = HostsRepository(lock, jedis)
    val hostsLogger = KotlinLogging.logger("Hosts")
    val hostsStorage = HostsStorage(hostsRepository, hostsLogger)

    val urlInfo = URLInfo("https://ecospace.org.ua")
    val host = URLParser().getHostname(urlInfo.link)
    frontier.update(host, listOf(urlInfo))

    val crawlingOperator = CrawlingOperator(frontier, visitedURLs, hostsStorage)
    crawlingOperator.run()
}