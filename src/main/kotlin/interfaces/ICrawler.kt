package interfaces

import crawler.Counter
import fetcher.Fetcher
import frontier.Frontier
import storage.hosts.HostsStorage
import storage.visitedurls.VisitedURLsStorage
import mu.KotlinLogging
import parser.urlparser.URLParser
import robots.RobotsManager

interface ICrawler {
    val primaryHost: String
    val fetcher: Fetcher
    val robotsManager: RobotsManager
    val urlParser: URLParser
    val frontier: Frontier
    val hostsStorage: HostsStorage
    val visitedURLsStorage: VisitedURLsStorage
    val kotlinLogging: KotlinLogging
    val counter: Counter
    fun start()
}