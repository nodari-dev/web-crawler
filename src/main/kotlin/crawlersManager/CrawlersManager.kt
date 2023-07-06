package crawlersManager

import crawler.Counter
import crawler.CrawlerUtils
import crawler.TerminalCrawler
import crawlersManager.Configuration.NUMBER_OF_CRAWLERS
import crawlersManager.Configuration.MODE
import dto.CrawlerModes
import fetcher.Fetcher
import frontier.Frontier
import interfaces.ICrawlersManager
import localStorage.HostsStorage
import localStorage.URLHashStorage
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots
import utils.Utils

class CrawlersManager : ICrawlersManager {
    private val threads = mutableListOf<Thread>()
    private val utils = Utils()
    private val crawlerUtils = CrawlerUtils()
    private val fetcher = Fetcher()
    private val robots = Robots()
    private val urlParser = URLParser()
    private val frontier = Frontier
    private val hostStorage = HostsStorage
    private val urlHashStorage = URLHashStorage
    private val kotlinLogging = KotlinLogging
    private val counter = Counter

    override fun addSeed(seed: String) {
        val formattedURL = utils.formatURL(seed)
        frontier.updateOrCreateQueue(urlParser.getMainURL(formattedURL), mutableListOf(formattedURL))
    }

    override fun startCrawling() {
        when (MODE) {
            CrawlerModes.CRAWLER -> println("Default crawler not implemented yet, choose TERMINAL_CRAWLER")
            CrawlerModes.TERMINAL_CRAWLER -> startTerminalVersion()
        }
    }

    private fun startTerminalVersion() {
        println(Illustrations.TerminalCrawler)
        for (i in 1..NUMBER_OF_CRAWLERS) {
            val crawler = TerminalCrawler(
                i,
                utils,
                crawlerUtils,
                fetcher,
                robots,
                urlParser,
                frontier,
                hostStorage,
                urlHashStorage,
                kotlinLogging,
                counter
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