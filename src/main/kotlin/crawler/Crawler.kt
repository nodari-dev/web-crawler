package crawler

import fetcher.Fetcher
import frontier.Frontier
import parser.Parser

class Crawler(
    private val id: Int,
    private val thread: Thread,
    private val config: Configuration,
    private val frontier: Frontier,
    private val fetcher: Fetcher,
    private val parser: Parser,
) {

    fun start() {
        thread.run { println("Thread ${thread.id}") }
        println("Started Crawler $id")
    }
}