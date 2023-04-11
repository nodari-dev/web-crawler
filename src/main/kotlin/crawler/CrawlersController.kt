package crawler

import UrlHashDataStore
import WebURL
import fetcher.Fetcher
import frontier.Frontier
import parser.Parser

class CrawlersController(private val config: Configuration) {

    private val frontier = Frontier()
    private val fetcher = Fetcher()
    private val parser = Parser()
    private val urlHashStorage = UrlHashDataStore()

    fun addSeeds(seeds: List<WebURL>) {
        frontier.addUrls(seeds)
    }

    fun start() {
        for (id: Int in 1..config.numberOfCrawlers) {
            Thread {
                Crawler(
                    id,
                    config,
                    frontier,
                    fetcher,
                    parser,
                    urlHashStorage
                ).start()
            }.start()

        }
    }
}