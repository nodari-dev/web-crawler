package application.crawler

import application.crawler.entities.CrawlerSettings
import application.interfaces.*
import configuration.Configuration.TIME_BETWEEN_FETCHING
import storage.interfaces.IFrontier
import core.dto.URLInfo
import mu.KLogger
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
    private val seoAnalyzer: ISEOAnalyzer,
    private val extractor: IExtractor,
    private val logger: KLogger
): Thread() {
    private var crawling = AtomicBoolean(false)
    private val settings = CrawlerSettings()

    fun id(newId: Int): Crawler {
        settings.id = newId
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
        } catch (e: InterruptedException){
            e.printStackTrace()
        }
    }

    private fun initCrawler(){
        frontier.assign(settings.id, settings.host)
        crawling.set(true)
        logger.info("#${settings.id} Imaaa started")
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
            killCrawler()
        } else{
            processURL(urlInfo)
        }
    }

    private fun killCrawler(){
        frontier.unassign(settings.id, settings.host)
        crawling.set(false)
        settings.host = ""
        logger.info("#${settings.id} Imaaa done")
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
        val seo = seoAnalyzer.generateSEO(html, urlInfo)
        if(seo != null){
            extractor.extractSEOData(seo, urlInfo)
        }

        val urlsInfoList = urlParser.getURLs(html)
        val packedURLs = filterAndPackedURLs(urlsInfoList)
        packedURLs.forEach{pack ->
            frontier.update(pack.key, pack.value)
        }
    }

    private fun filterAndPackedURLs(urlsInfoList: List<URLInfo>): MutableMap<String, MutableList<URLInfo>> {
        val uniqueURLs = urlsInfoList.toSet().toList()
        val urlsInfoListOnlyNew = visitedURLs.filterByNewOnly(uniqueURLs)
        val filteredByDepthURLs = urlsInfoListOnlyNew.filter { urlInfo ->
            urlInfo.link.length + 2 - urlInfo.link.replace(
                "/",
                ""
            ).length < 50
        }

        return urlPacker.pack(filteredByDepthURLs)
    }
}