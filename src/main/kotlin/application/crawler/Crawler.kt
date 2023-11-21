package application.crawler

import application.crawler.entities.CrawlerSettings
import application.interfaces.*
import configuration.Configuration.TIME_BETWEEN_FETCHING
import storage.interfaces.IFrontier
import core.dto.URLInfo
import mu.KLogger
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import storage.interfaces.IHostsStorage
import storage.interfaces.IVisitedURLs
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.random.Random

class Crawler(
    private val frontier: IFrontier,
    private val visitedURLs: IVisitedURLs,
    private val hostsStorage: IHostsStorage,
    private val fetcher: IFetcher,
    private val urlParser: IURLParser,
    private val robotsParser: IRobotsParser,
    private val urlPacker: IURLPacker,
    private val extractor: IDataExtractor,
    private val logger: KLogger
): Thread() {
    private var crawling = AtomicBoolean(false)
    private val settings = CrawlerSettings()

    fun id(newId: Int): Crawler {
        settings.id = newId
        return this
    }

    fun recoveryMode(): Crawler {
        settings.recoveryMode = true
        return this
    }

    fun host(newHost: String): Crawler {
        settings.host = newHost
        return this
    }

    fun isCrawling(): Boolean{
        return crawling.get()
    }

    override fun run() {
        initCrawler()
        setupHost()

        try{
            while (crawling.get()){
                sleep(TIME_BETWEEN_FETCHING + Random.nextLong(0, TIME_BETWEEN_FETCHING))
                crawl()
            }
            logger.info("#${settings.id} Imaaa done")
        } catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

    private fun initCrawler(){
        if(!settings.recoveryMode){
            frontier.assign(settings.id, settings.host)
        }
        crawling.set(true)
        if(settings.recoveryMode){
            logger.info("#${settings.id} Imaaa started in recovery mode!")

        } else{
            logger.info("#${settings.id} Imaaa started")
        }
    }

    private fun setupHost(){
        if(hostsStorage.doesHostExist(settings.host)){
            val robots = fetcher.downloadSanitizedHTML("https://" + settings.host + "/robots.txt")
            if(robots != null){
                val disallowedURLs = robotsParser.getRobotsDisallowed(robots)
                hostsStorage.updateHost(settings.host, disallowedURLs)
            }
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
            val html = fetcher.downloadSanitizedHTML(urlInfo.link)
            if(html != null){
                processHTML(html, urlInfo)
            }
        }
    }

    private fun isURLInfoValid(urlInfo: URLInfo?): Boolean{
        if(urlInfo == null){
            return false
        }

        val isAllowed = hostsStorage.isURLAllowed(settings.host, urlInfo)
        val isNew = visitedURLs.isValid(urlInfo.hash)
        return isAllowed && isNew
    }

    private fun processHTML(html: String, urlInfo: URLInfo){
        extractor.extractSEODataToFile(html, urlInfo.link)

        val urlsInfoList = urlParser.getURLs(html)
        val packedURLs = filterAndPackedURLs(urlsInfoList)
        packedURLs.forEach{pack ->
            frontier.update(pack.key, pack.value)
        }
    }

    private fun filterAndPackedURLs(urlsInfoList: List<URLInfo>): MutableMap<String, MutableList<URLInfo>> {
        val urlsInfoListOnlyNew = visitedURLs.filterByNewOnly(urlsInfoList)
        val filteredByDepthURLs = urlsInfoListOnlyNew.filter { urlInfo ->
            urlInfo.link.length + 2 - urlInfo.link.replace(
                "/",
                ""
            ).length < 50
        }

        return urlPacker.pack(filteredByDepthURLs)
    }
}