package interfaces

import dto.CrawlerTypes

interface ICrawlersManager {
    val crawlerType: CrawlerTypes
    fun start()
}