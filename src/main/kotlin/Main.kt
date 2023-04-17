import crawler.Configuration
import crawler.CrawlersController
import dto.WebURL

fun main() {
    val seeds: List<WebURL> = listOf(
        WebURL("https://ecospace.org.ua", 0.1),
        WebURL("https://magecloud.agency", 0.1)
    )

    val controller = CrawlersController()
    controller.addSeeds(seeds)
    controller.startCrawling()
}