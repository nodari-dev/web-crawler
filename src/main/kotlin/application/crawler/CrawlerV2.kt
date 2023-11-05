package application.crawler

import application.crawler.entities.CrawlerConfig
import application.crawler.entities.CrawlerStatus
import application.interfaces.ICrawlerV2
import application.interfaces.IFetcher
import application.interfaces.IURLParser
import storage.interfaces.IFrontierV2

class CrawlerV2(
    private val config: CrawlerConfig,
    private val frontier: IFrontierV2,
    private val fetcher: IFetcher,
    private val urlParser: IURLParser
):Runnable, ICrawlerV2 {
    private val status = CrawlerStatus(
        isAlive = false,
        isWorking = false
    )

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
        while (status.isAlive){
            val webLink = frontier.pullWebLink(config.host)
            if(webLink != null){
            val html = fetcher.getPageHTML(webLink.url)
//                if(html != null){
//                    val webLinkList = urlParser.getURLs(html)
//                    webLinkList.forEach{webLink -> frontier.updateOrCreateQueue(
//                        urlParser.getHostWithProtocol(webLink.url), webLink.url
//                        )
//                    }
//                }
            }
//            if(count < 1){
//                val result = frontier.pullURL(config.host)
//                println(result)
//                count++
//            } else{
//                status.isAlive = false
//            }
        }
    }
}