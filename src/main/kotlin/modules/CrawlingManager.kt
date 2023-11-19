package modules

import application.crawler.Crawler
import application.crawler.URLPacker
import application.fetcher.Fetcher
import application.parser.robotsparser.RobotsParser
import application.parser.urlparser.URLParser
import modules.interfaces.ICrawlersManagerV2
import mu.KotlinLogging
import storage.interfaces.IFrontier
import storage.interfaces.IHostsStorage
import storage.interfaces.IVisitedURLs

class CrawlingManager(
    private val frontier: IFrontier,
    private val visitedURLs: IVisitedURLs,
    private val hostsStorage: IHostsStorage,
): ICrawlersManagerV2 {
    private val MAX_NUMBER_OF_CRAWLERS = 15

    private val fetcher = Fetcher()
    private val urlParser = URLParser()
    private val robotsParser = RobotsParser()
    private val urlPacker = URLPacker()
    private val crawlerLogger = KotlinLogging.logger("Crawler")

    private var idCounter = -1
    private val setId: () -> Int = {
        idCounter += 1
        idCounter
    }
    private val crawlers = Array(MAX_NUMBER_OF_CRAWLERS) { createCrawler().id(setId()) }

    override fun run() {
        // Note: deez nuts work btw
        // Initialize crawling
        val startingQueue = frontier.getAvailableQueue()
        val startingCrawler = crawlers[0]
        if(startingQueue != null){
            startingCrawler.host(startingQueue).start()
            waitForCrawler(startingCrawler)
        }

        var index = 1
        while (!allCrawlersFinished()){
            if (crawlers[index].isCrawling()){
                index +=1
                if (index >= MAX_NUMBER_OF_CRAWLERS){
                    index = 0
                }
                continue
            }

            val queueName = frontier.getAvailableQueue()
            if(queueName != null) {
                try {
                    crawlers[index].host(queueName).start()
                    waitForCrawler(crawlers[index])
                } catch (e: Exception){
                    crawlers[index].join()
                    crawlers[index] = createCrawler().id(index).host(queueName)
                    crawlers[index].start()
                    waitForCrawler(crawlers[index])
                }
            }
        }
        crawlers.forEach { crawler -> crawler.join() }
    }

    private fun createCrawler(): Crawler{
        return Crawler(frontier, visitedURLs, hostsStorage, fetcher, urlParser, robotsParser, urlPacker, crawlerLogger)
    }

    private fun allCrawlersFinished(): Boolean{
        return crawlers.all{crawlerV2 -> !crawlerV2.isCrawling()}
    }

    private fun waitForCrawler(crawler: Crawler){
        while (!crawler.isCrawling()) continue
    }
}