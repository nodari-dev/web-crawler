package application.crawler

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CrawlerV2Test {

    private val crawler = CrawlerV2(0, "")

    @Test
    fun `returns status`(){
        val alive = crawler.getStatus().isAlive
        val isWorking = crawler.getStatus().isWorking
        assertEquals(false, alive)
        assertEquals(false, isWorking)
    }
}