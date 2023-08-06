package interfaces

interface ICommunicationManager {
    fun start()
    fun stopCrawler(crawler: Thread)
    fun addSeed(seed: String)
    fun addHost(host: String)
}