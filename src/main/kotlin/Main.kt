import crawlersManager.CrawlersManager
import dto.CrawlerModes
import dto.Page
import java.net.URL

fun main() {
    val manager = CrawlersManager(CrawlerModes.TERMINAL_CRAWLER)
    manager.addSeed("https://ecospace.org.ua")
    manager.startCrawling()

}
