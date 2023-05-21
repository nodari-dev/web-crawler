package crawler

import SingleHostCrawl
import Page
import frontier.Frontier

class Crawler(
    private val id: Int,
): Thread() {
    private val frontier = Frontier

    override fun run() {
        println("Started Crawler $id on thread ${currentThread().id}")

//        val url = frontier.getUrl()
//        if(url != null){
//            val node = Page(url)
//            val bfs = SingleHostCrawl(node)
//            bfs.start()
//        } else{
//            // this stupid developer will update it and provide better connection
//            println("Frontier has no urls to give to thread: ${currentThread().id}")
//        }
    }
}