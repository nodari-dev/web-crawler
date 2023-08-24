package interfaces

import crawler.Counter
import crawler.URLValidator
import fetcher.Fetcher
import frontier.Frontier
import storage.hosts.HostsStorage
import storage.visitedurls.VisitedURLsStorage
import mu.KotlinLogging
import parser.urlparser.URLParser
import robots.RobotsManager

interface ICrawler {
    val primaryHost: String
    fun start()
}