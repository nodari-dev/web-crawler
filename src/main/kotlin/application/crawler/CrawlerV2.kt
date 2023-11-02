package application.crawler

import application.crawler.entities.CrawlerConfig
import application.crawler.entities.CrawlerStatus
import application.interfaces.ICrawlerV2
import storage.interfaces.IFrontierV2

class CrawlerV2(
    private val config: CrawlerConfig,
    private val frontier: IFrontierV2,
):Runnable, ICrawlerV2 {
    private val status = CrawlerStatus(isAlive = false, isWorking = false)

    override fun getStatus(): CrawlerStatus {
        return status
    }

    override fun getConfig(): CrawlerConfig {
        return config
    }

    override fun reassign(newHost: String) {
        config.host = newHost
    }

    override fun terminate() {
        status.isAlive = false
        status.isWorking = false
    }

    override fun run() {
        styingAlive()
        crawl()
    }

    private fun styingAlive(){
        status.isAlive = true
        status.isWorking = true
    }

    private fun crawl(){
        println("Hello fucking world! Here is my data: $config")
        var count = 0
        while (status.isAlive){
            if(count < 5){
                val result = frontier.pullURL(config.host)
                frontier.updateOrCreateQueue(config.host, result.url + "_$count")
                frontier.updateOrCreateQueue(config.host, result.url + "_$count")
                println(frontier.pullURL(config.host))
                count++
            } else{
                status.isAlive = false
            }
        }
    }

}