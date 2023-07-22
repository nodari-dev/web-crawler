import crawlersManager.CrawlersManager

fun main() {
    val manager = CrawlersManager()
    manager.addSeed("https://ecospace.org.ua")
    manager.startCrawling()
}
