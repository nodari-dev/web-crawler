package interfaces

interface IController {
    fun start()
    fun addStartingPointURLs(seeds: List<String>)
    fun addHost(host: String)
    fun stopCrawler(crawler: Thread)
}