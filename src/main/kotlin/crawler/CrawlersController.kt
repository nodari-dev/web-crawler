package crawler

import frontier.Frontier

class CrawlersController() {
    private val frontier = Frontier()
    private val threads = mutableListOf<Thread>()

    fun start(){
        for(i in 0..1){
            val crawler = Crawler(i, frontier)
            threads.add(crawler)
            crawler.start()
        }

        threads.forEach { thread ->
            thread.join()
        }
    }

}