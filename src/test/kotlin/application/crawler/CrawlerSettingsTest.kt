package application.crawler

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

import configuration.Configuration

class CrawlerSettingsTest{
    private val crawlerSettings = CrawlerSettings()

    @Test
    fun `setNewDelay works correct`(){
        crawlerSettings.setNewDelay(-1)
        assertEquals(Configuration.CRAWLING_DELAY, crawlerSettings.getDelay())

        crawlerSettings.setNewDelay(10)
        assertEquals(1000, crawlerSettings.getDelay())
    }
}
