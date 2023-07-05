import crawlersManager.CrawlersManager
import dto.CrawlerModes
import dto.Host
import hostsStorage.HostsStorage

fun main() {
    val manager = CrawlersManager(CrawlerModes.TERMINAL_CRAWLER)
    manager.addSeed("https://ecospace.org.ua")
    manager.startCrawling()
}
