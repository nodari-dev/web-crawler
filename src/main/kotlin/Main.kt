import crawler.Configuration
import crawler.CrawlersController

fun main() {
    val config = Configuration(
        1,
        1,
        "frontq",
        "backq",
        "visited",
    )

    val seeds: List<WebURL> = listOf(WebURL("host.com", 0.1))

    val controller = CrawlersController(config)
    controller.addSeeds(seeds)
    controller.start()
}