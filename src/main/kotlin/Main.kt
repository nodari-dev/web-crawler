import application.CrawlerFactory
import application.fetcher.Fetcher
import application.parser.urlparser.URLParser
import core.dto.URLData
import infrastructure.repository.RedisRepositoryV2
import modules.CrawlersManagerV2
import modules.QueuesManager
import mu.KotlinLogging
import redis.clients.jedis.JedisPool
import storage.frontier.FrontierV2
import java.util.concurrent.locks.ReentrantLock

fun main() {
//    SeedsManager.startCrawling(listOf("https://ecospace.org.ua"))
    val url = "https://ecospace.org.ua"
    val host = URLParser().getHostWithProtocol(url)

    val jedis = JedisPool("localhost", 6379).resource
    val reentrantLock = ReentrantLock()
    val redisGateway = RedisRepositoryV2(reentrantLock, jedis)
    val frontier = FrontierV2(redisGateway)
    val fetcher = Fetcher(KotlinLogging.logger("fetcher"))
    val urlParser = URLParser()

    frontier.updateOrCreateQueue(host, URLData(url).url)

    val crawlerFactory = CrawlerFactory(frontier, fetcher, urlParser)

    val crawlersManagerV2 = CrawlersManagerV2(crawlerFactory)
    // DO NOT TOUCH IT
//    crawlersManagerV2.requestCrawlerInitializationAndGetId(host)

    val queuesManager = QueuesManager(crawlersManagerV2, frontier)
}