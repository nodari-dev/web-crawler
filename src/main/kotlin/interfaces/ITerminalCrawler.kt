package interfaces


import crawler.Counter
import frontier.Frontier
import urlHashStorage.URLHashStorage

interface ITerminalCrawler {
    val id: Int
    val frontier: Frontier
    val urlHashStorage: URLHashStorage
    val counter: Counter
    fun updateStorage(hash: Int)
    fun start()
}