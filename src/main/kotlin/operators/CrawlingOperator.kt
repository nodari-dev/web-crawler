package operators

import application.crawler.Crawler
import application.crawler.URLPacker
import application.extractor.Extractor
import application.fetcher.Fetcher
import application.parser.robotsparser.RobotsParser
import application.parser.urlparser.URLParser
import configuration.Configuration.MAX_NUMBER_OF_CRAWLERS
import operators.interfaces.ICrawlingOperator
import mu.KotlinLogging
import storage.interfaces.IFrontier
import storage.interfaces.IHostsStorage
import storage.interfaces.IVisitedURLs

class CrawlingOperator(
    private val frontier: IFrontier,
    private val visitedURLs: IVisitedURLs,
    private val hostsStorage: IHostsStorage,
): ICrawlingOperator {
    private val fetcher = Fetcher()
    private val urlParser = URLParser()
    private val robotsParser = RobotsParser()
    private val urlPacker = URLPacker()
    private val extractor = Extractor()
    private val crawlerLogger = KotlinLogging.logger("Crawler")

    private var idCounter = -1
    private val setId: () -> Int = {
        idCounter += 1
        idCounter
    }
    private val crawlers = Array(MAX_NUMBER_OF_CRAWLERS) { createCrawler().id(setId()) }
    private val crawlingStartupActions = CrawlingStartupActions(crawlers, frontier)

    override fun run() {
        // Note: deez nuts work btw
        val recoveredFrontierData = frontier.getQueuesWithActiveCrawlers()
        if(recoveredFrontierData.isNotEmpty()){
            crawlingStartupActions.runRecoveryMode(recoveredFrontierData)
        } else{
            crawlingStartupActions.runStartingMode()
        }

        runDefaultCrawling()
    }

    private fun allCrawlersFinished(): Boolean{
        return crawlers.all{crawlerV2 -> !crawlerV2.isCrawling()}
    }
    private fun runDefaultCrawling() {
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
        return Crawler(
            frontier,
            visitedURLs,
            hostsStorage,
            fetcher,
            urlParser,
            robotsParser,
            urlPacker,
            extractor,
            crawlerLogger
        )
    }

    private fun waitForCrawler(crawler: Crawler){
        while (!crawler.isCrawling()) continue
    }
}