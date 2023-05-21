package crawler

import frontier.Frontier

class Crawler(
    private val id: Int,
): Thread() {
    private val frontier = Frontier

    override fun run() {
        println("Started Crawler $id on thread ${currentThread().id}")
    }
}