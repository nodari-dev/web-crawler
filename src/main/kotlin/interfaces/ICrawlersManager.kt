package interfaces

import dto.CrawlerModes

interface ICrawlersManager {
    fun addSeed(seed: String)
    fun startCrawling()
}