package application.crawler

import application.crawler.entities.CrawlerSettings
import application.extractor.Extractor
import application.interfaces.IFetcher
import application.interfaces.IURLPacker
import application.interfaces.IURLParser
import storage.interfaces.IFrontierV2
import core.dto.URLInfo
import storage.interfaces.IVisitedURLs
import kotlin.random.Random

class CrawlerV2(
    private val frontier: IFrontierV2,
    private val visitedURLs: IVisitedURLs,
    private val fetcher: IFetcher,
    private val urlParser: IURLParser,
    private val urlPacker: IURLPacker,
): Thread() {
    private var crawling = false
    private val settings = CrawlerSettings()

    fun setId(newId: Int){
        settings.id = newId
    }

    fun setHost(newHost: String){
        settings.host = newHost
    }

    fun isCrawling(): Boolean{
        return crawling
    }

    override fun run() {
        crawling = true
        println("IMAAA HERE ${settings.id}")
        frontier.assign(settings.id, settings.host)

        try{
            while (crawling){
                sleep(5000 + Random.nextLong(0, 5000))
                crawl()
            }
            println("IMAAA DONE ${settings.id}")
        } catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

    private fun crawl (){
        val urlInfo = frontier.pullFrom(settings.host)
        if(urlInfo == null){
            frontier.unassign(settings.id, settings.host)
            crawling = false
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