package crawler

import BreadthFirstSearch
import Node
import frontier.Frontier

class Crawler(
    private val id: Int,
): Thread() {
    private val frontier = Frontier

    override fun run() {
        println("Started Crawler $id on thread ${currentThread().id}")
        val url = frontier.getUrl()
        if(url != null){
            val node = Node(url)
            val bfs = BreadthFirstSearch(node)
            bfs.traverse()
        } else{
            // this stupid developer will update it and provide better connection
            println("Frontier has no urls to give to thread: ${currentThread().id}")
        }
    }
}