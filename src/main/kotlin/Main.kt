import crawlersManager.CrawlersManager
import dto.CrawlerModes

fun main() {
    val manager = CrawlersManager(CrawlerModes.TERMINAL_CRAWLER)
    manager.addSeed("https://ecospace.org.ua")
    manager.startCrawling()

}
