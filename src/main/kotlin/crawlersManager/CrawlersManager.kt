package crawlersManager

import HostConnector
import analyzer.DataAnalyzer
import crawler.Counter
import crawler.CrawlerUtils
import crawler.Crawler
import crawlersManager.Configuration.NUMBER_OF_CRAWLERS
import dto.FormattedURL
import fetcher.Fetcher
import frontier.Frontier
import interfaces.ICrawlersManager
import localStorage.HostsStorage
import localStorage.VisitedURLs
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

class CrawlersManager : ICrawlersManager {
    private val threads = mutableListOf<Thread>()
    private val urlParser = URLParser()
    private val frontier = Frontier

    override fun addSeed(seed: String) {
        val host = urlParser.getHostWithProtocol(seed)
        frontier.updateOrCreateQueue(host, FormattedURL(seed))
    }

    override fun startCrawling() {
        println(Illustrations.CrawlerStarted)
        for (i in 1..NUMBER_OF_CRAWLERS) {
            val crawler = Crawler(
                i,
                CrawlerUtils(),
                Fetcher(),
                Robots(),
                HostConnector(i),
                DataAnalyzer(),
                urlParser,
                frontier,
                HostsStorage,
                VisitedURLs,
                KotlinLogging,
                Counter
            )
            threads.add(crawler)
            crawler.start()
        }

        joinThreads()
    }

    private fun joinThreads() {
        threads.forEach { thread ->
            thread.join()
        }
    }
}