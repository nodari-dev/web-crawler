package application.crawler

import application.crawler.entities.CrawlerConfig
import application.crawler.entities.CrawlerStatus
import application.interfaces.ICrawlerV2
import storage.interfaces.IFrontierV2

class CrawlerV2(
    private val config: CrawlerConfig,
    private val frontier: IFrontierV2,
):Runnable, ICrawlerV2 {
    private var isAlive = false
    private var isWorking = false

    override fun getStatus(): CrawlerStatus {
        return CrawlerStatus(isAlive, isWorking)
    }

    override fun getConfig(): CrawlerConfig {
        return config
    }

    override fun terminate() {
        TODO("Not yet implemented")
    }

    override fun run() {
        println("Hello fucking world! Here is my data: $config")

        val result = frontier.pullURL(config.host)
        frontier.updateOrCreateQueue(config.host, result.url + "/-_-")

        println(frontier.pullURL(config.host))
    }

}