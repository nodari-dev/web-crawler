package interfaces

import crawler.CrawlersFactory
import crawler.URLValidator
import crawlingManager.CrawlingManager
import fetcher.Fetcher
import mu.KLogger
import parser.urlparser.URLParser
import storage.frontier.Frontier
import storage.hosts.HostsStorage
import storage.url.URLStorage

interface ICrawler {
    val primaryHost: String
    var logger: KLogger
    val fetcher: Fetcher
    val urlValidator: URLValidator
    val urlParser: URLParser
    val crawlingManager: CrawlingManager
    val crawlersFactory: CrawlersFactory
    val hostsStorage: HostsStorage
    val urlStorage: URLStorage
    val frontier: Frontier
    fun start()
}