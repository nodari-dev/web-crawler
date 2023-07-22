package crawler

import analyzer.DataAnalyzer
import fetcher.Fetcher
import frontier.Frontier
import localStorage.HostsStorage
import localStorage.URLHashStorage
import mu.KotlinLogging
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import parser.urlParser.URLParser
import robots.Robots

class CrawlerTest {
    private lateinit var terminalCrawler: Crawler

    private val crawlerUtils = CrawlerUtils()
    private val fetcher = Fetcher()
    private val robots = Robots()
    private val urlParser = URLParser()
    private val dataAnalyzer = DataAnalyzer()
    private val frontier = Frontier
    private val hostStorage = HostsStorage
    private val urlHashStorage = URLHashStorage
    private val kotlinLogging = KotlinLogging
    private val counter = Counter


    @BeforeEach
    fun init() {
        terminalCrawler = Crawler(
            0,
            crawlerUtils,
            fetcher,
            robots,
            dataAnalyzer,
            urlParser,
            frontier,
            hostStorage,
            urlHashStorage,
            kotlinLogging,
            counter
        )

        terminalCrawler.start()
    }

    @Test
    fun `connects to frontier queue by specific host`() {

    }

    fun `get urls from frontier queue by specific host`() {

    }

    fun `disconnects from frontier queue if no urls left`() {

    }

    fun `sends new urls to frontier queue connected by host`() {}

}