package interfaces

interface ICrawlersFactory {
    fun requestCrawlerInitialization(host: String)
    fun removeTerminatedCrawler(crawler: Thread)
}