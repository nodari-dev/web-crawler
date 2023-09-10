package interfaces

import crawler.CrawlersManager
import crawler.URLValidator
import dataExtractor.DataExtractor
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
    val dataExtractor: DataExtractor
    val crawlersFactory: CrawlersManager
    val hostsStorage: HostsStorage
    val urlStorage: URLStorage
    val frontier: Frontier
    fun start()
}