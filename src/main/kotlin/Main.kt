import crawlersManager.CrawlersManager
import dto.URLRecord
import frontier.Frontier

fun main() {
    val manager = CrawlersManager()
    manager.addSeed("https://ecospace.org.ua")
    manager.addSeed("https://ecospace.org.ua/wp-admin/")
    manager.startCrawling()
}
