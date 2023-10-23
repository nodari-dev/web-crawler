package core.interfaces.components

interface ICrawlersManager {
    fun requestCrawlerInitialization(host: String)
    fun removeTerminatedCrawler(crawler: Thread)
}