import modules.CrawlersManagerV2
import modules.SeedsManager

fun main() {
//    SeedsManager.startCrawling(listOf("https://ecospace.org.ua"))
    val crawlersManagerV2 = CrawlersManagerV2()
    crawlersManagerV2.requestCrawlerInitialization("host")
}