package interfaces

import analyzer.DataAnalyzer
import crawler.Counter
import fetcher.Fetcher
import frontier.Frontier
import localStorage.HostsStorage
import localStorage.VisitedURLs
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

interface ICrawler {
    val primaryHost: String
    val fetcher: Fetcher
    val robots: Robots
    val dataAnalyzer: DataAnalyzer
    val urlParser: URLParser
    val frontier: Frontier
    val hostStorage: HostsStorage
    val urlHashStorage: VisitedURLs
    val kotlinLogging: KotlinLogging
    val counter: Counter
    fun start()
}