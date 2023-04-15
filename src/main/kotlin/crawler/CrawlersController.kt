package crawler

import UrlHashDataStore
import WebURL
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

    fun start() {
        for (id: Int in 1..config.numberOfCrawlers) {
            Thread {
                Crawler(
                    id,
                    config,
                    fetcher,
                    parser,
                    urlHashStorage
                ).start()
            }.start()

        }
    }
}