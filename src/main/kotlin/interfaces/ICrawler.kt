package interfaces

import crawler.Counter
import fetcher.Fetcher
import frontier.FrontierRedis
import storages.hostsStorage.HostsStorage
import storages.visitedURLsStorage.VisitedURLsStorage
import mu.KotlinLogging
import parser.urlParser.URLParser
import robots.Robots

interface ICrawler {
    val primaryHost: String
    val fetcher: Fetcher
    val robots: Robots
    val urlParser: URLParser
    val frontier: FrontierRedis
    val hostsStorage: HostsStorage
    val visitedURLsStorage: VisitedURLsStorage
    val kotlinLogging: KotlinLogging
    val counter: Counter
    fun start()
}