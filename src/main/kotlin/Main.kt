import application.CrawlerFactory
import application.crawler.URLPacker
import application.fetcher.Fetcher
import application.parser.urlparser.URLParser
import core.dto.URLData
import infrastructure.repository.FrontierRepository
import modules.CrawlersManagerV2
import modules.QueuesManager
import mu.KotlinLogging
import redis.clients.jedis.JedisPool
import storage.frontier.FrontierV2
import java.util.concurrent.locks.ReentrantLock

fun main() {
//    SeedsManager.startCrawling(listOf("https://ecospace.org.ua"))
    val urlData = URLData("https://ecospace.org.ua")
    val host = URLParser().getHostWithProtocol(urlData.url)
    val reentrantLock = ReentrantLock()

    val jedis = JedisPool("localhost", 6379).resource

    val frontierRepository = FrontierRepository(reentrantLock, jedis)
    val kotlinLogger = KotlinLogging.logger("Frontier")
    val frontier = FrontierV2(frontierRepository, kotlinLogger)

    val fetcherLogger = KotlinLogging.logger("fetcher")
    val fetcher = Fetcher(fetcherLogger)
    val urlParser = URLParser()
    val urlPacker = URLPacker()

    frontier.updateOrCreateQueue(host, listOf(urlData.url))

    val crawlerFactory = CrawlerFactory(frontier, fetcher, urlParser, urlPacker)

    val crawlersManagerV2 = CrawlersManagerV2(crawlerFactory)

    val queuesManager = QueuesManager(crawlersManagerV2, frontier, urlPacker)

    queuesManager.provideSeeds(listOf(urlData))
}