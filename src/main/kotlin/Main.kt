import application.CrawlerFactory
import infrastructure.repository.RedisRepository
import modules.CrawlersManagerV2
import modules.QueuesManager
import storage.frontier.FrontierV2

fun main() {
//    SeedsManager.startCrawling(listOf("https://ecospace.org.ua"))

    val redisGateway = RedisRepository
    val frontier = FrontierV2(redisGateway)

    // seed manager imitation

    frontier.updateOrCreateQueue("host", "url")

    val crawlerFactory = CrawlerFactory(frontier)

    val crawlersManagerV2 = CrawlersManagerV2(crawlerFactory)
    crawlersManagerV2.requestCrawlerInitializationAndGetId("host")
//    crawlersManagerV2.requestCrawlerTermination(0)
//    crawlersManagerV2.requestCrawlerReassignToAnotherQueue(0, "newHost")

    val queuesManager = QueuesManager(crawlersManagerV2)

//    queuesManager.startMonitoring()

//    frontier.register(queuesManager)
//    frontier.testMe()
}