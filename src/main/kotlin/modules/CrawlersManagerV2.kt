package modules

import application.crawler.CrawlerV2
import application.crawler.entities.CrawlerConfig
import application.interfaces.ICrawlerV2
import modules.interfaces.ICrawlersManagerV2
import storage.interfaces.IFrontierV2

class CrawlersManagerV2(private val frontier: IFrontierV2): ICrawlersManagerV2 {
    private val crawlers: MutableList<ICrawlerV2> = mutableListOf()

    override fun requestCrawlerInitializationAndGetId(host: String): Int {
        val crawler = generateCrawler(host)
        crawlers.add(crawler)
        val thread = Thread(crawler as Runnable)
        thread.start()
        thread.join()

        return crawler.getConfig().id
    }

    private fun generateCrawler(host: String): ICrawlerV2{
        val crawlerId = generateCrawlerId()
        val config = CrawlerConfig(crawlerId, host)
        return CrawlerV2(config, frontier)
    }

    private fun generateCrawlerId(): Int{
        return if(crawlers.isEmpty()){
            0
        } else{
            crawlers.size + 1
        }
    }

    override fun requestCrawlerTermination(id: Int) {
        crawlers[id].terminate()
        crawlers.removeAt(id)
    }

    override fun requestCrawlerReassignToAnotherQueue(id: Int, host: String) {
        crawlers[id].reassign(host)
    }

    override fun requestAllCrawlers(): Int {
        return crawlers.size
    }
}