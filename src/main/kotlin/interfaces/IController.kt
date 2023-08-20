package interfaces

interface IController {
    fun start()
    fun addStartingPointURLs(seeds: List<String>)
    fun notifyWithNewQueue(host: String)
    fun stopCrawler(crawler: Thread)
}