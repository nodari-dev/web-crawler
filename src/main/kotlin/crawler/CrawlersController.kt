package crawler

import dto.WebURL
import frontier.Frontier

class CrawlersController() {
    private val frontier = Frontier
    private val config = Configuration

    fun addSeeds(urls: List<WebURL>){
        frontier.addUrls(urls)
    }

    fun startCrawling() {

        for (id: Int in 1..config.numberOfCrawlers) {
            Crawler(id).start()
        }
    }
}