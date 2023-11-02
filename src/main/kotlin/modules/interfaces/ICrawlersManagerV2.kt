package modules.interfaces

interface ICrawlersManagerV2 {
    fun requestCrawlerInitializationAndGetId(host: String): Int
    fun requestCrawlerTermination(id: Int)
    fun requestCrawlerReassignToAnotherQueue(id: Int, host: String)
    fun requestAllCrawlers(): Int
}