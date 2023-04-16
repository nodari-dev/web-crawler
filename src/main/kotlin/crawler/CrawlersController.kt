package crawler

import UrlHashDataStore
import dto.WebURL
import fetcher.Fetcher
import frontier.Frontier
import parser.Parser

class CrawlersController(private val config: Configuration) {

    private val fetcher = Fetcher(config)
    private val parser = Parser()
    private val frontier = Frontier()
    private val urlHashStorage = UrlHashDataStore()

    fun addSeeds(urls: List<WebURL>){
        frontier.addUrls(urls)
    }

    fun startCrawling() {

        for (id: Int in 1..config.numberOfCrawlers) {
            Crawler(
                id,
                config,
                fetcher,
                parser,
                frontier,
                urlHashStorage
            ).start()
        }
    }
}