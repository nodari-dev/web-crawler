package crawler

import frontier.Frontier

class CrawlersController() {
    private val frontier = Frontier
    private val config = Configuration

    fun addSeeds(urls: List<String>){
        frontier.addUrls(urls)
    }

    fun startCrawling() {

        for (id: Int in 1..config.numberOfCrawlers) {
//            frontier.backQInserted.connect(crawler::onBackQInserted)
//            crawler.urlAdded.connect(frontier::onUrlAdded)
            Crawler(id).start()
        }
    }
}