package interfaces

import crawler.Counter
import fetcher.Fetcher
import frontier.FrontierRedis
import localStorage.HostsStorage
import localStorage.VisitedURLs
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

interface ICrawler {
    val primaryHost: String
    val fetcher: Fetcher
    val robots: Robots
    val urlParser: URLParser
    val frontier: FrontierRedis
    val hostStorage: HostsStorage
    val urlHashStorage: VisitedURLs
    val kotlinLogging: KotlinLogging
    val counter: Counter
    fun start()
}