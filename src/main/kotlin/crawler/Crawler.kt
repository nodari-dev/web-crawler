package crawler

import BreathFirstSearch
import UrlHashDataStore
import Vertex
import fetcher.Fetcher
import frontier.Frontier
import parser.Parser

class Crawler(
    private val id: Int,
    private val config: Configuration,
    private val frontier: Frontier,
    private val fetcher: Fetcher,
    private val parser: Parser,
    private val urlHashStorage: UrlHashDataStore
) {

    fun start() {
        println("Started Crawler $id ${Thread.currentThread()}")
        val vertex = Vertex("https://magecloud.agency")
        val bfs = BreathFirstSearch(vertex, urlHashStorage, fetcher)
        bfs.traverse()
    }
}