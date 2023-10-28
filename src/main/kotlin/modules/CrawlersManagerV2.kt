package modules

import application.crawler.CrawlerV2
import application.interfaces.ICrawlerV2
import modules.interfaces.ICrawlersManagerV2
import storage.interfaces.IFrontier

class CrawlersManagerV2(private val frontier: IFrontier): ICrawlersManagerV2 {
    private val crawlers: MutableList<ICrawlerV2> = mutableListOf()

    override fun requestCrawlerInitialization(host: String): Int {
        val crawlerId = generateCrawlerId()
        val crawlerV2: ICrawlerV2 = CrawlerV2(crawlerId, host)
        crawlers.add(crawlerV2)

        val thread = Thread(crawlerV2 as Runnable)
        thread.start()
        thread.join()

        return crawlerId
    }

    private fun generateCrawlerId(): Int{
        return if(crawlers.isEmpty()){
            0
        } else{
            crawlers.size + 1
        }
    }

    override fun requestCrawlerTermination(id: Int) {
        crawlers.removeAt(id)
    }

    override fun requestCrawlerReassignToAnotherQueue(id: Int, host: String) {
        TODO("Not yet implemented")
    }

    override fun requestAllCrawlers(): Int {
        return crawlers.size
    }
}