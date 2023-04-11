package crawler

import WebURL
import fetcher.Fetcher
import frontier.Frontier
import parser.Parser

class CrawlersController(private val config: Configuration) {

    private val frontier = Frontier()
    private val fetcher = Fetcher()
    private val parser = Parser()

    fun addSeeds(seeds: List<WebURL>) {
        frontier.addUrls(seeds)
    }

    fun start() {
        for (id: Int in 1..config.numberOfCrawlers) {
            Crawler(
                id,
                Thread(),
                config,
                frontier,
                fetcher,
                parser
            ).start()
        }
    }
}