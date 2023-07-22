package interfaces

import analyzer.DataAnalyzer
import crawler.Counter
import crawler.CrawlerUtils
import fetcher.Fetcher
import frontier.Frontier
import localStorage.HostsStorage
import localStorage.URLHashStorage
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

interface ICrawler {
    val id: Int
    val crawlerUtils: CrawlerUtils
    val fetcher: Fetcher
    val robots: Robots
    val dataAnalyzer: DataAnalyzer
    val urlParser: URLParser
    val frontier: Frontier
    val hostStorage: HostsStorage
    val urlHashStorage: URLHashStorage
    val kotlinLogging: KotlinLogging
    val counter: Counter
    fun start()
}