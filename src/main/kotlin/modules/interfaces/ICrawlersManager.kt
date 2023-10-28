package modules.interfaces

interface ICrawlersManager {
    fun requestCrawlerInitialization(host: String)
    fun removeTerminatedCrawler(crawler: Thread)
}