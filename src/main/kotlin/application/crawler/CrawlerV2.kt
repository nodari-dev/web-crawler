package application.crawler

import application.crawler.entities.CrawlerConfig
import application.interfaces.IFetcher
import application.interfaces.IURLPacker
import application.interfaces.IURLParser
import storage.interfaces.IFrontierV2
import core.configuration.Configuration.TIME_BETWEEN_FETCHING

class CrawlerV2(
    id: Int,
    private val frontier: IFrontierV2,
    private val fetcher: IFetcher,
    private val urlParser: IURLParser,
    private val urlPacker: IURLPacker,
):Runnable {
    private val isALive = true
    private val config = CrawlerConfig(id)

    override fun run() {
        println("Started ${config.id}")

        try{
            while (isALive){
                crawl()
            }
        } catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

    private fun crawl(){
        if(config.host == null){
            Thread.sleep(5000)
            val host = frontier.getAvailableQueue()
            if(host != null){
                frontier.assign(config.id, host)
                config.host = host
            } else{
                crawl()
            }
        } else{
            Thread.sleep(5000)
            processURL(frontier.pullFrom(config.host!!))
        }
        Thread.sleep(5000)
        processURL(frontier.pullFrom(config.host!!))
    }

    private fun processURL(url: String?){
        if(url != null){
            Thread.sleep(TIME_BETWEEN_FETCHING)
            val html = fetcher.getPageHTML(url)
            if(html != null){
                processHTML(html)
            }
        }
    }

    private fun processHTML(html: String){
        val webLinkList = urlParser.getURLs(html)
        val packedURLs = urlPacker.pack(webLinkList)
        packedURLs.forEach{pack ->
            frontier.update(pack.key, pack.value)
        }
    }
}