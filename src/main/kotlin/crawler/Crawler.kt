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
    }

    fun start(){
        println("Started Crawler $crawlerId")
        val urls = listOf<String>(
            "https://ecospace.org.ua",
            "https://sometext.org.ua",
            "https://somt.org.ua",
            "https://somtasdasd.org.ua"
        )

        frontier.addUrls(urls)
    }
}