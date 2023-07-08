package crawler

import dto.URLRecord
import localStorage.URLHashStorage
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class CrawlerUtilsTest {

    @Test
    fun `checks if url is new`() {
        val urlRecord = URLRecord("url")
        val urlRecordTwo = URLRecord("url_test")
        val crawlerUtils = CrawlerUtils()
        val urlHashStorage = URLHashStorage
        urlHashStorage.add(urlRecord.getUniqueHash())

        Assertions.assertEquals(false, crawlerUtils.isURLNew(urlRecord))
        Assertions.assertEquals(true, crawlerUtils.isURLNew(urlRecordTwo))
    }
}