package crawler

import fetcher.Fetcher
import frontier.Frontier
import parser.Parser

class Crawler {

    private var crawlerId: Int? = null
    private lateinit var crawlerThread: Thread

    private val fetcher = Fetcher()
    private val parser = Parser()
    private val frontier = Frontier()

    fun initialize(id: Int, thread: Thread){
        crawlerId = id
        crawlerThread = thread

        println("Crawler $crawlerId is ready to go!")
    }
}