package application

import application.crawler.CrawlerV2
import application.crawler.entities.CrawlerConfig
import application.interfaces.ICrawlerFactory
import application.interfaces.ICrawlerV2
import application.interfaces.IFetcher
import application.interfaces.IURLParser
import storage.interfaces.IFrontierV2

class CrawlerFactory(
    private val frontier: IFrontierV2,
    private val fetcher: IFetcher,
    private val urlParser: IURLParser
): ICrawlerFactory {
    override fun generateCrawler(config: CrawlerConfig): ICrawlerV2 {
        return CrawlerV2(config, frontier, fetcher, urlParser)
    }
}