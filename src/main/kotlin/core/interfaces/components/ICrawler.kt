package core.interfaces.components

import components.crawler.CrawlersManager
import components.crawler.URLValidator
import components.dataExtractor.DataExtractor
import components.fetcher.Fetcher
import components.parser.urlparser.URLParser
import components.storage.frontier.Frontier
import components.storage.hosts.HostsStorage
import components.storage.url.URLStorage
import mu.KLogger

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