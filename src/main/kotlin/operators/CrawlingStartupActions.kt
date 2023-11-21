package operators

import application.crawler.Crawler
import storage.interfaces.IFrontier

class CrawlingStartupActions(private val crawlers: Array<Crawler>, private val frontier: IFrontier) {
    fun runRecoveryMode(recoveredData: MutableList<List<String>>){
        recoveredData.forEach{crawlerQueueRelation ->
            val index = crawlerQueueRelation[0].toInt()
            val host = crawlerQueueRelation[1]
            crawlers[index].recoveryMode().host(host).start()
            waitForCrawler(crawlers[index])
        }
    }

    fun runStartingMode(){
        val startingQueue = frontier.getAvailableQueue()
        val startingCrawler = crawlers[0]
        if(startingQueue != null){
            startingCrawler.host(startingQueue).start()
            waitForCrawler(startingCrawler)
        }
    }

    private fun waitForCrawler(crawler: Crawler){
        while (!crawler.isCrawling()) continue
    }
}