package interfaces

import dto.URLRecord

interface ICrawlersManager {
    fun addSeed(seed: String)
    fun startCrawling()
}