package crawlersManager

import crawler.Counter
import crawler.Crawler
import crawler.TerminalCrawler
import crawlersManager.Configuration.NUMBER_OF_CRAWLERS
import dto.CrawlerModes
import frontier.Frontier
import interfaces.ICrawlersManager
import urlHashStorage.URLHashStorage

class CrawlersManager(
    override val crawlerMode: CrawlerModes
): ICrawlersManager {
    private val frontier = Frontier()
    private val threads = mutableListOf<Thread>()
    private val hashStorage = URLHashStorage()
    private val counter = Counter()

   override fun addSeed(seed: String){
        frontier.addURL(seed)
    }

    override fun startCrawling(){
        when(crawlerMode){
            CrawlerModes.CRAWLER -> println("Default crawler not implemented yet, choose TERMINAL_CRAWLER")
            CrawlerModes.TERMINAL_CRAWLER -> startTerminalVersion()
        }
    }

    private fun startTerminalVersion(){
        println("---TERMINAL CRAWLER STARTED---")
        println(Illustrations.TerminalCrawler)
        for(i in 1..NUMBER_OF_CRAWLERS){
            val crawler = TerminalCrawler(i, frontier, hashStorage, counter)
            threads.add(crawler)
            crawler.start()
        }

        joinThreads()
    }

    private fun joinThreads(){
        threads.forEach { thread ->
            thread.join()
        }
    }
}