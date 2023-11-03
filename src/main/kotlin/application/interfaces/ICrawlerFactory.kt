package application.interfaces

import application.crawler.entities.CrawlerConfig

interface ICrawlerFactory {
    fun generateCrawler(config: CrawlerConfig): ICrawlerV2
}