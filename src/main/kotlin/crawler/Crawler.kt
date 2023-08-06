package crawler

import analyzer.DataAnalyzer
import crawlersManager.CrawlersManager
import dto.FormattedURL
import dto.URLRecord
import fetcher.Fetcher
import frontier.Frontier
import interfaces.ICrawler
import localStorage.HostsStorage
import localStorage.VisitedURLs
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

class Crawler(
    override val primaryHost: String,
    override val fetcher: Fetcher,
    override val robots: Robots,
    override val dataAnalyzer: DataAnalyzer,
    override val urlParser: URLParser,
    override val frontier: Frontier,
    override val hostStorage: HostsStorage,
    override val urlHashStorage: VisitedURLs,
    override val kotlinLogging: KotlinLogging,
    override val counter: Counter
) : ICrawler, Thread() {
    private val logger = kotlinLogging.logger("Crawler:${id}")
    private var done = false

    override fun run() {
        startCrawl()
    }

    private fun startCrawl(){
//        processRobotsTxt()
        println("Hello, I'm crawler $primaryHost")
        while (!done) {
            processNewFrontierRecord()
        }
    }

    private fun processNewFrontierRecord(){
        val urlRecord = frontier.pullURLRecord(primaryHost)
        if(urlRecord == null){
            sendMurderRequest()
        } else{
            if(canProcessInternalURL(urlRecord)) fetchHTML(urlRecord)
        }
    }
//
    private fun sendMurderRequest(){
        logger.info ("KILL ME PLEASE")
        CrawlersManager.murder(this)
        done = true
        logger.info ("Thank you, now I'm free... God bless your soul")
        return
    }

    private fun fetchHTML(urlRecord: URLRecord){
        counter.increment()
        VisitedURLs.add(urlRecord.getUniqueHash())

        val html = fetcher.getPageContent(urlRecord.getURL())
        html?.let{
            val urls = urlParser.getURLs(html)
//            dataAnalyzer.getPageStats(html)
            processFetchedURLs(urls)
        }
    }

    private fun processFetchedURLs(urls: List<FormattedURL>) {
        val uniqueFormattedURLs = urls.toSet()
        uniqueFormattedURLs.forEach{formattedURL ->
            val host = urlParser.getHostWithProtocol(formattedURL.value)
            if(canProcessExternalURL(host, formattedURL)){
                frontier.updateOrCreateQueue(host, formattedURL)
            }
        }
    }

    private fun canProcessInternalURL(urlRecord: URLRecord): Boolean{
       return isURLValid(primaryHost, urlRecord.formattedURL)
    }

    private fun canProcessExternalURL(host: String, formattedURL: FormattedURL?): Boolean{
        return isURLValid(host, formattedURL)
    }

    private fun isURLValid(host: String, formattedURL: FormattedURL?): Boolean{
        if(formattedURL == null){
            return false
        }

        val urlRecord = URLRecord(formattedURL)
        val isNew = VisitedURLs.doesNotExist(urlRecord.getUniqueHash())
        val isAllowed = HostsStorage.isURLAllowed(host, formattedURL.value)
        return isNew && isAllowed
    }
}