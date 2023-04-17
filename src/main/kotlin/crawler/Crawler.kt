package crawler

import BreadthFirstSearch
import UrlHashDataStore
import Node
import fetcher.Fetcher
import frontier.Frontier
import parser.Parser


class Crawler(
    private val id: Int,
    private val config: Configuration,
    private val fetcher: Fetcher,
    private val parser: Parser,
    private val frontier: Frontier,
    private val urlHashStorage: UrlHashDataStore
): Thread() {

    override fun run() {
        println("Started Crawler $id on thread ${currentThread().id}")
        val node = Node(frontier.getUrl())
        val bfs = BreadthFirstSearch(node, urlHashStorage, fetcher)
        bfs.traverse()
    }
}