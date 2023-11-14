package modules

import application.crawler.CrawlerV2
import application.interfaces.*
import modules.interfaces.ICrawlersManagerV2
import storage.interfaces.IFrontierV2
import java.util.concurrent.Executors

class CrawlingManager(
    private val frontier: IFrontierV2,
    private val fetcher: IFetcher,
    private val urlParser: IURLParser,
    private val urlPacker: IURLPacker
): ICrawlersManagerV2 {
    private val MAX_NUMBER_OF_CRAWLERS = 2
    private val crawlersPool = Executors.newFixedThreadPool(MAX_NUMBER_OF_CRAWLERS)

    override fun start() {
        for (i in 0 until  MAX_NUMBER_OF_CRAWLERS){
            crawlersPool.execute{
                CrawlerV2(i, frontier, fetcher, urlParser, urlPacker).run()
            }
        }

        crawlersPool.shutdown()
    }
}