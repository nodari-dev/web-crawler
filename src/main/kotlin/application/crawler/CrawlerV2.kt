package application.crawler

import application.crawler.entities.CrawlerSettings
import application.extractor.Extractor
import application.interfaces.IFetcher
import application.interfaces.IURLPacker
import application.interfaces.IURLParser
import storage.interfaces.IFrontierV2
import core.dto.URLInfo
import mu.KLogger
import storage.interfaces.IVisitedURLs
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

class CrawlerV2(
    private val frontier: IFrontierV2,
    private val visitedURLs: IVisitedURLs,
    private val fetcher: IFetcher,
    private val urlParser: IURLParser,
    private val urlPacker: IURLPacker,
    private val logger: KLogger
): Thread() {
    private var crawling = AtomicBoolean(false)
    private val settings = CrawlerSettings()

    fun id(newId: Int): CrawlerV2 {
        settings.id = newId
        return this
    }

    fun host(newHost: String): CrawlerV2 {
        settings.host = newHost
        return this
    }

    fun isCrawling(): Boolean{
        return crawling.get()
    }

    override fun run() {
        frontier.assign(settings.id, settings.host)
        crawling.set(true)
        logger.info("#${settings.id} Imaaa started")

        try{
            while (crawling.get()){
                sleep(5000 + Random.nextLong(0, 5000))
                crawl()
            }
            logger.info("#${settings.id} Imaaa done")
        } catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

    private fun crawl (){
        val urlInfo = frontier.pullFrom(settings.host)
        if(urlInfo == null){
            frontier.unassign(settings.id, settings.host)
            crawling.set(false)
            settings.host = ""
        } else{
            processURL(urlInfo)
        }
    }

    private fun processURL(urlInfo: URLInfo){
        if(isURLInfoValid(urlInfo)){
            visitedURLs.update(urlInfo)
            val html = fetcher.getPageHTML(urlInfo.link)
            if(html != null){
                processHTML(html, urlInfo)
            }
        }
    }

    private fun isURLInfoValid(urlInfo: URLInfo?): Boolean{
        if(urlInfo == null){
            return false
        }
        return visitedURLs.isValid(urlInfo.hash)
    }

    private fun processHTML(html: String, urlInfo: URLInfo){
        Extractor().extractSEODataToFile(html, urlInfo.link)

        val urlsInfoList = urlParser.getURLs(html)
        val urlsInfoListOnlyNew = visitedURLs.filterByNewOnly(urlsInfoList)
        val packedURLs = urlPacker.pack(urlsInfoListOnlyNew)
        packedURLs.forEach{pack ->
            frontier.update(pack.key, pack.value)
        }
    }
}