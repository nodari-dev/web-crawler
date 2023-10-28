package application.crawler

import application.crawler.entities.CrawlerStatus
import application.interfaces.ICrawlerV2

class CrawlerV2(
    private val id: Int,
    private var host: String
):Runnable, ICrawlerV2 {
    private var isAlive = false
    private var isWorking = false

    override fun getStatus(): CrawlerStatus {
        return CrawlerStatus(isAlive, isWorking)
    }

    override fun terminate() {
        TODO("Not yet implemented")
    }

    override fun run() {
        println("Hello fucking world! Here is my data: $id, $host")
    }

}