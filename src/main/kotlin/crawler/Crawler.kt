package crawler

import BreadthFirstSearch
import UrlHashDataStore
import Vertex
import fetcher.Fetcher
import frontier.Frontier
import parser.Parser


class Crawler(
    private val id: Int,
    private val config: Configuration,
    private val fetcher: Fetcher,
    private val parser: Parser,
    private val urlHashStorage: UrlHashDataStore
) {

    fun start() {
        println("Started Crawler $id ${Thread.currentThread()}")
        val vertex = Vertex("https://ecospace.org.ua")
        val bfs = BreadthFirstSearch(vertex, urlHashStorage, fetcher)
        bfs.traverse()
    }
}