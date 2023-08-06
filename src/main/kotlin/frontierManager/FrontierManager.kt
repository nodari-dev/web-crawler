package frontierManager

import crawlersManager.CrawlersManager
import dto.FormattedURL
import frontier.Frontier
import parser.urlParser.URLParser

object FrontierManager{
    private val crawlersManager = CrawlersManager
    private val urlParser = URLParser()

    fun addNewHost(host: String){
        crawlersManager.provideNewHost(host)
    }

    fun addSeed(seed: String) {
        val host = urlParser.getHostWithProtocol(seed)
        Frontier.updateOrCreateQueue(host, FormattedURL(seed))
    }
}