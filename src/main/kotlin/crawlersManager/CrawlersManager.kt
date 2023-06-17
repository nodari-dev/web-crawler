package crawlersManager

import crawler.TerminalCrawler
import crawlersManager.Configuration.NUMBER_OF_CRAWLERS
import dto.CrawlerModes
import dto.Page
import frontier.Frontier
import interfaces.ICrawlersManager

class CrawlersManager(
    override val crawlerMode: CrawlerModes
): ICrawlersManager {
    private val threads = mutableListOf<Thread>()

   override fun addSeed(seed: String){
        Frontier.addURL(Page(seed).url)
    }

    override fun startCrawling(){
        when(crawlerMode){
            CrawlerModes.CRAWLER -> println("Default crawler not implemented yet, choose TERMINAL_CRAWLER")
            CrawlerModes.TERMINAL_CRAWLER -> startTerminalVersion()
        }
    }

    private fun startTerminalVersion(){
        println(Illustrations.TerminalCrawler)
        for(i in 1..NUMBER_OF_CRAWLERS){
            val crawler = TerminalCrawler(i)
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