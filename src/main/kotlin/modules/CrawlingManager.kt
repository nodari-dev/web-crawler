package modules

import application.crawler.CrawlerV2
import application.interfaces.*
import modules.interfaces.ICrawlersManagerV2
import storage.interfaces.IFrontierV2
import storage.interfaces.IVisitedURLs

class CrawlingManager(
    private val frontier: IFrontierV2,
    private val visitedURLs: IVisitedURLs,
    private val fetcher: IFetcher,
    private val urlParser: IURLParser,
    private val urlPacker: IURLPacker
): ICrawlersManagerV2, Thread() {
    private val MAX_NUMBER_OF_CRAWLERS = 15
    private val crawlers = Array(MAX_NUMBER_OF_CRAWLERS) {CrawlerV2(frontier, visitedURLs, fetcher, urlParser, urlPacker)}

    override fun start() {
        for (i in 0 until  MAX_NUMBER_OF_CRAWLERS){
            crawlers[i].setId(i)
        }
        var crawlerIndex = 0

        // DO NOT TOUCH IT
        val startingQueue = frontier.getAvailableQueue()
        val startingCrawler = crawlers[crawlerIndex]
        if(startingQueue != null){
            startingCrawler.setHost(startingQueue)
            startingCrawler.start()

            crawlerIndex +=1
        }

        sleep(1000)

        // DO NOT TOUCH IT
        crawlerIndex = 0
        while (!allCrawlersFinished()){
            while (crawlers[crawlerIndex].isCrawling()){
                crawlerIndex +=1
                if (crawlerIndex >= MAX_NUMBER_OF_CRAWLERS){
                    crawlerIndex = 0
                }
            }

            val queueName = frontier.getAvailableQueue()

            if(queueName != null) {
                val crawler = crawlers[crawlerIndex]
                crawler.setHost(queueName)
                try {
                    crawler.start()
                } catch (e: Exception){
                    crawler.join()
                    val newCrawler = CrawlerV2(frontier, visitedURLs, fetcher, urlParser, urlPacker)
                    crawlers[crawlerIndex] = newCrawler
                    newCrawler.setId(crawlerIndex)
                    newCrawler.setHost(queueName)
                    newCrawler.start()
                }

                sleep(1000)
            }
        }
    }

    private fun allCrawlersFinished(): Boolean{
        return crawlers.all{crawlerV2 -> !crawlerV2.isCrawling()}
    }
}