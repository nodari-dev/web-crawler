package crawler

import analyzer.DataAnalyzer
import fetcher.Fetcher
import frontier.Frontier
import localStorage.HostsStorage
import localStorage.VisitedURLs
import mu.KotlinLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import parser.urlParser.URLParser
import robots.Robots

class CrawlerTest {
    private lateinit var terminalCrawler: Crawler
//
//
//    @BeforeEach
//    fun init() {
//        terminalCrawler = Crawler(
//            0,
//            crawlerUtils,
//            fetcher,
//            robots,
//            dataAnalyzer,
//            urlParser,
//            frontier,
//            hostStorage,
//            urlHashStorage,
//            kotlinLogging,
//            counter
//        )
//
//        terminalCrawler.start()
//    }

    @Test
    fun `connects to frontier queue by specific host`() {

    }

    fun `get urls from frontier queue by specific host`() {

    }

    fun `disconnects from frontier queue if no urls left`() {

    }

    fun `sends new urls to frontier queue connected by host`() {}

}