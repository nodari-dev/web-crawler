package frontierManager

import crawlersManager.CrawlersManager
import dto.FormattedURL
import frontier.Frontier
import parser.urlParser.URLParser

object FrontierManager{
    private val crawlersManager = CrawlersManager
    private val urlParser = URLParser()
    private val frontier = Frontier
    private val seedURls = mutableListOf<String>()

    fun addNewHost(host: String){
        crawlersManager.provideNewHost(host)
    }

    fun addSeed(seed: String) {
        seedURls.add(seed)
    }

    fun start(){
        seedURls.forEach { seed ->
            val host = urlParser.getHostWithProtocol(seed)
            frontier.updateOrCreateQueue(host, FormattedURL(seed))
        }
    }

}