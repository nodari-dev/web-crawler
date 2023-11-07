import application.parser.urlparser.URLParser
import infrastructure.repository.FrontierRepository
import redis.clients.jedis.JedisPool
import storage.frontier.FrontierV2
import java.util.concurrent.locks.ReentrantLock

fun main() {
//    SeedsManager.startCrawling(listOf("https://ecospace.org.ua"))
    val url = "https://ecospace.org.ua"
    val host = URLParser().getHostWithProtocol(url)
    val reentrantLock = ReentrantLock()

    val jedis = JedisPool("localhost", 6379).resource
//    jedis.rpush(host, url)
//    jedis.hset("frontier", "queues", host)
//    val lists = jedis.hget("frontier", "queues")
//    println(lists.contains("not_exist"))
//    println(lists.contains(url))
//
//    val list = jedis.hget("frontier", "queues")
//    println(jedis.lrange(list, 0, -1))
//    val result = jedis.lpop(list)
//    println(jedis.lrange(list, 0, -1))
//    println("result $result")




    val frontierRepository = FrontierRepository(reentrantLock, jedis)
//
//    val frontier = FrontierV2(frontierRepository)
//    frontier.updateOrCreateQueue(host, listOf(url))
//    println(frontier.pullWebLink(host))
//    frontier.deleteQueue(host)

    val expectedResult = mutableMapOf(
        "host-urls" to "host",
        "host-crawlers" to "host-crawlers"
    )

    println(
        expectedResult.filter { item -> item.key.contains("crawlers") }
    )
//    val fetcher = Fetcher(KotlinLogging.logger("fetcher"))
//    val urlParser = URLParser()
//
//    frontier.updateOrCreateQueue(host, URLData(url).url)
//
//    val crawlerFactory = CrawlerFactory(frontier, fetcher, urlParser)
//
//    val crawlersManagerV2 = CrawlersManagerV2(crawlerFactory)
//    // DO NOT TOUCH IT
////    crawlersManagerV2.requestCrawlerInitializationAndGetId(host)
//
//    val queuesManager = QueuesManager(crawlersManagerV2, frontier)
}