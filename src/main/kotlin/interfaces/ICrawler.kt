package interfaces

import HostConnector
import analyzer.DataAnalyzer
import crawler.Counter
import crawler.CrawlerUtils
import fetcher.Fetcher
import frontier.Frontier
import localStorage.HostsStorage
import localStorage.VisitedURLs
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

interface ICrawler {
    val id: Int
    val crawlerUtils: CrawlerUtils
    val fetcher: Fetcher
    val robots: Robots
    val hostConnector: HostConnector
    val dataAnalyzer: DataAnalyzer
    val urlParser: URLParser
    val frontier: Frontier
    val hostStorage: HostsStorage
    val urlHashStorage: VisitedURLs
    val kotlinLogging: KotlinLogging
    val counter: Counter
    fun start()
}