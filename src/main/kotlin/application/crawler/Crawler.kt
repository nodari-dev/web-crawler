package application.crawler

import mu.KLogger
import java.util.concurrent.atomic.AtomicBoolean

import core.dto.RobotsData
import core.dto.URLInfo
import application.interfaces.*
import storage.interfaces.IFrontier
import storage.interfaces.IRobotsStorage
import storage.interfaces.IVisitedURLs
import infrastructure.repository.interfaces.ISEORepository


class Crawler(
    private val frontier: IFrontier,
    private val visitedURLs: IVisitedURLs,
    private val robotsStorage: IRobotsStorage,
    private val fetcher: IFetcher,
    private val urlParser: IURLParser,
    private val robotsParser: IRobotsParser,
    private val urlPacker: IURLPacker,
    private val seoAnalyzer: ISEOAnalyzer,
    private val seoRepository: ISEORepository,
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
        processRobots()

        try{
            while (crawling.get()){
                crawl()
                sleep(settings.getDelay())
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

    private fun processRobots(){
        val robots = robotsStorage.get(settings.host)
        if(robots == null){
            processNewRobots()
        } else{
            settings.setNewDelay(robots.crawlDelay)
            settings.bannedURLs = robots.bannedURLs
        }
    }

    private fun processNewRobots(){
        val fetchedRobots = fetcher.downloadHTML("http://" + settings.host + "/robots.txt")
        if(fetchedRobots == null){
            robotsStorage.update(settings.host, RobotsData(emptyList()))
        } else{
            val robotsData = robotsParser.getRobotsData(fetchedRobots)
            robotsStorage.update(settings.host, robotsData)
            settings.setNewDelay(robotsData.crawlDelay)
            settings.bannedURLs = robotsData.bannedURLs
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
        settings.toDefaults()
        logger.info("#${settings.id} Imaaa done")
    }

    private fun processURL(urlInfo: URLInfo){
        if(isURLInfoValid(urlInfo)){
            visitedURLs.update(urlInfo)
            val html = fetcher.downloadHTML(urlInfo.link)
            if(html != null){
                processHTML(html, urlInfo)
            }
        }
    }

    private fun isURLInfoValid(urlInfo: URLInfo?): Boolean{
        if(urlInfo == null){
            return false
        }

        val isAllowed = settings.bannedURLs.none { bannedUrl -> urlInfo.link.contains(bannedUrl.link) }
        val isNew = visitedURLs.isValid(urlInfo.hash)
        return isAllowed && isNew
    }

    private fun processHTML(html: String, urlInfo: URLInfo){
        val seo = seoAnalyzer.generateSEO(html, urlInfo)
        if(seo != null){
            seoRepository.put(seo)
        }

        val urlsInfoList = urlParser.getURLs(html)
        val packedURLs = filterAndPackURLs(urlsInfoList)
        packedURLs.forEach{pack ->
            frontier.update(pack.key, pack.value)
        }
    }

    private fun filterAndPackURLs(urlsInfoList: List<URLInfo>): MutableMap<String, MutableList<URLInfo>> {
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