package crawler

import dto.FormattedURL
import dto.URLRecord
import localStorage.HostsStorage
import localStorage.URLHashStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CrawlerUtilsTest {
    private val crawlerUtils = CrawlerUtils()
    private val urlHashStorage = URLHashStorage
    private val hostsStorage = HostsStorage

    @Test
    fun `checks if url is new and not blocked`() {
        val url1 = FormattedURL("url")
        val url2 = FormattedURL("url_test")
        val host = "host"

        val bannedURLs = listOf(url1)

        urlHashStorage.add(URLRecord(url1).getUniqueHash())
        hostsStorage.addHostRecord(host,bannedURLs)

        Assertions.assertEquals(false, crawlerUtils.isURLValid(host, url1))
        Assertions.assertEquals(true, crawlerUtils.isURLValid(host, url2))
    }
}