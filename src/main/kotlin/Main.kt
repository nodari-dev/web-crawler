import crawler.Configuration
import crawler.CrawlersController
import frontier.Frontier

fun main() {
    val config = Configuration(
        2,
        1000,
        "frontq",
        "backq",
        "visited",
        )

    val seeds: List<WebURL> = listOf(WebURL("host.com", 0.1))

    val controller = CrawlersController(config)
    controller.addSeeds(seeds)
    controller.start()
}