package interfaces

interface ICommunicationManager {
    fun start()
    fun addStartingPointURLs(seeds: List<String>)
    fun requestCrawlerInitialization(host: String)
    fun requestCrawlerTermination(crawler: Thread)
}