package operators

import application.crawler.Crawler
import application.crawler.URLPacker
import application.fetcher.Fetcher
import application.htmlAnalyzer.SEOAnalyzer
import application.interfaces.ISubscriber
import application.parser.robotsparser.RobotsParser
import application.parser.urlparser.URLParser
import configuration.Configuration.MAX_NUMBER_OF_CRAWLERS
import infrastructure.repository.interfaces.ISEORepository
import operators.interfaces.ICrawlingOperator
import mu.KotlinLogging
import storage.interfaces.IFrontier
import storage.interfaces.IRobotsStorage
import storage.interfaces.IVisitedURLs

class CrawlingOperator(
    private val frontier: IFrontier,
    private val visitedURLs: IVisitedURLs,
    private val hostsStorage: IRobotsStorage,
    private val seoRepository: ISEORepository,
): ICrawlingOperator, ISubscriber {
    private val fetcher = Fetcher()
    private val urlParser = URLParser()
    private val robotsParser = RobotsParser()
    private val urlPacker = URLPacker()
    private val seoAnalyzer = SEOAnalyzer()
    private val crawlerLogger = KotlinLogging.logger("Crawler")

    private var idCounter = -1
    private val setId: () -> Int = {
        idCounter += 1
        idCounter
    }
    private val crawlers = Array(MAX_NUMBER_OF_CRAWLERS) { createCrawler().id(setId()) }

    init {
        frontier.subscribe(this)
    }

    override fun run() {
        startCrawling()
    }

    override fun trigger() {
        if(allCrawlersFinished()){
            startCrawling()
        }
    }

    private fun startCrawling(){
        // Note: deez nuts work btw
        val startingQueue = frontier.getAvailableQueue()
        if(startingQueue != null){
            manipulateCrawler(0, startingQueue)
            monitorAndManipulateCrawlers()
            crawlers.forEach { crawler -> crawler.join() }
        }
    }

    private fun monitorAndManipulateCrawlers() {
        var index = 0
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
                    manipulateCrawler(index, queueName)
                } catch (e: Exception){
                    crawlers[index].join()
                    crawlers[index] = createCrawler().id(index).host(queueName)
                    crawlers[index].start()
                    waitForCrawler(crawlers[index])
                }
            }
        }
    }

    private fun manipulateCrawler(index: Int, host: String){
        if(crawlers[index].state.name == "TERMINATED") {
            crawlers[index] = createCrawler().id(index).host(host)
            crawlers[index].start()
        } else{
            crawlers[index].host(host)
            crawlers[index].start()
        }
        waitForCrawler(crawlers[index])
    }

    private fun allCrawlersFinished(): Boolean{
        return crawlers.all{crawler -> !crawler.isCrawling()}
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
            seoAnalyzer,
            seoRepository,
            crawlerLogger
        )
    }

    private fun waitForCrawler(crawler: Crawler){
        while (!crawler.isCrawling()) continue
    }
}