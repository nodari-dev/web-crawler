package interfaces

interface ICrawlersManager {
    fun addSeed(seed: String)
    fun startCrawling()
}