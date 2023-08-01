package crawler

import dto.FormattedURL
import dto.URLRecord
import localStorage.HostsStorage
import localStorage.VisitedURLs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CrawlerUtilsTest {
    private val crawlerUtils = CrawlerUtils()
    private val urlHashStorage = VisitedURLs
    private val hostsStorage = HostsStorage

    @Test
    fun `checks if url is new and not blocked`() {
        val url1 = FormattedURL("url")
        val url2 = FormattedURL("url_test")
        val host = "host"

        val bannedURLs = listOf(url1)

        urlHashStorage.add(URLRecord(url1).getUniqueHash())
        hostsStorage.addHostRecord(host,bannedURLs)

        Assertions.assertEquals(false, crawlerUtils.canProcessURL(host, url1))
        Assertions.assertEquals(true, crawlerUtils.canProcessURL(host, url2))
        Assertions.assertEquals(false, crawlerUtils.canProcessURL(host, null))
    }
}