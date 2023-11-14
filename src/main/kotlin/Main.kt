import application.crawler.URLPacker
import application.fetcher.Fetcher
import application.parser.urlparser.URLParser
import core.dto.URLData
import infrastructure.repository.FrontierRepository
import modules.CrawlingManager
import mu.KotlinLogging
import redis.clients.jedis.JedisPool
import storage.frontier.FrontierV2
import java.util.concurrent.locks.ReentrantLock

fun main() {
    val reentrantLock = ReentrantLock()
    val jedis = JedisPool("localhost", 6379).resource
    val frontierRepository = FrontierRepository(reentrantLock, jedis)
    val frontierLogger = KotlinLogging.logger("Frontier")
    val frontier = FrontierV2(frontierRepository, frontierLogger)

    val urlData = URLData("https://ecospace.org.ua")
    val host = URLParser().getHostWithProtocol(urlData.url)
    frontier.update(host, listOf(urlData.url))

    val fetcherLogger = KotlinLogging.logger("Fetcher")
    val fetcher = Fetcher(fetcherLogger)
    val urlParser = URLParser()
    val urlPacker = URLPacker()
    val crawlingManager = CrawlingManager(frontier, fetcher, urlParser, urlPacker)
    crawlingManager.start()
}