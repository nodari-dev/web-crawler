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
    private val crawlers = Array(MAX_NUMBER_OF_CRAWLERS) {
        CrawlerV2(frontier, visitedURLs, fetcher, urlParser, urlPacker)
    }

    override fun start() {
        for (i in 0 until  MAX_NUMBER_OF_CRAWLERS){
            crawlers[i].id(i)
        }
        var crawlerIndex = 0

        // DO NOT TOUCH IT
        val startingQueue = frontier.getAvailableQueue()
        val startingCrawler = crawlers[crawlerIndex]
        if(startingQueue != null){
            startingCrawler.host(startingQueue).start()
            while (!startingCrawler.isCrawling()) continue
            crawlerIndex +=1
        }
//        sleep(1000)
        // DO NOT TOUCH IT

        while (!allCrawlersFinished()){
            while (crawlers[crawlerIndex].isCrawling()){
                crawlerIndex +=1
                if (crawlerIndex >= MAX_NUMBER_OF_CRAWLERS){
                    crawlerIndex = 0
                }
            }

            val queueName = frontier.getAvailableQueue()

            if(queueName != null) {
                try {
                    crawlers[crawlerIndex].host(queueName).start()
                    while (!crawlers[crawlerIndex].isCrawling()) continue
                } catch (e: Exception){
                    crawlers[crawlerIndex].join()

                    crawlers[crawlerIndex] = CrawlerV2(frontier, visitedURLs, fetcher, urlParser, urlPacker)
                        .id(crawlerIndex)
                        .host(queueName)
                    crawlers[crawlerIndex].start()
                    while (!crawlers[crawlerIndex].isCrawling()) continue
                    println(crawlers[crawlerIndex].isCrawling())

                }

//                sleep(1000)
            }
        }
        crawlers.forEach { crawler -> crawler.join() }
    }

    private fun allCrawlersFinished(): Boolean{
        return crawlers.all{crawlerV2 -> !crawlerV2.isCrawling()}
    }
}