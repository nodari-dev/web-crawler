package crawler

import crawler.Configuration.NUMBER_OF_CRAWLERS
import dto.CrawlerTypes
import frontier.Frontier
import interfaces.IController

class Controller(
    override val crawlerType: CrawlerTypes
): IController {
    private val frontier = Frontier()
    private val threads = mutableListOf<Thread>()

    override fun start(){
        when(crawlerType){
            CrawlerTypes.CRAWLER -> startDefaultVersion()
            CrawlerTypes.TERMINAL_CRAWLER -> startTerminalVersion()
        }
    }

    private fun startTerminalVersion(){

    }

    private fun startDefaultVersion(){
        for(i in 0..NUMBER_OF_CRAWLERS){
            val crawler = Crawler(i, frontier)
            threads.add(crawler)
            crawler.start()
        }

        threads.forEach { thread ->
            thread.join()
        }
    }
}