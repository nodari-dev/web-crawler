package crawler

import crawler.Configuration.NUMBER_OF_CRAWLERS
import frontier.Frontier

class CrawlersController() {
    private val frontier = Frontier()
    private val threads = mutableListOf<Thread>()

    fun start(){
        for(i in 0..NUMBER_OF_CRAWLERS){
            val crawler = Crawler(i, frontier)
            threads.add(crawler)
            crawler.start()
        }

        threads.forEach { thread ->
            thread.join()
        }
    }

}