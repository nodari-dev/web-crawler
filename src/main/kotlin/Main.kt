import infrastructure.memoryGateways.RedisMemoryGateway
import modules.CrawlersManagerV2
import modules.QueuesManager
import storage.frontier.FrontierV2

fun main() {
//    SeedsManager.startCrawling(listOf("https://ecospace.org.ua"))

    val redisGateway = RedisMemoryGateway
    val frontier = FrontierV2(redisGateway)

    // seed manager imitation

    frontier.updateOrCreateQueue("host", "url")

    val crawlersManagerV2 = CrawlersManagerV2(frontier)
    val queuesManager = QueuesManager(crawlersManagerV2, frontier)
    queuesManager.startMonitoring()
//    crawlersManagerV2.requestCrawlerInitialization("host")
}