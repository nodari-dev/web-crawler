package interfaces

import dto.CrawlerModes

interface ICrawlersManager {
    val crawlerMode: CrawlerModes
    fun addSeed(seed: String)
    fun startCrawling()
}