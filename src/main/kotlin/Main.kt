import crawlersManager.CrawlersManager
import dto.CrawlerModes

fun main() {
    val manager = CrawlersManager()
    manager.addSeed("https://ecospace.org.ua/sea")
    manager.startCrawling()
}
