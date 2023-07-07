package interfaces

import dto.URLRecord

interface ICrawlersManager {
    fun addSeed(seed: URLRecord)
    fun startCrawling()
}