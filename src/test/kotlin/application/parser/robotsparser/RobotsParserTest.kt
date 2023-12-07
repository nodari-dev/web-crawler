package application.parser.robotsparser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import core.dto.RobotsData
import core.dto.URLInfo

class RobotsParserTest {
    private val robotsParser = RobotsParser()

    @Test
    fun `returns host data from robots txt`() {
        val expectedResult = RobotsData(listOf(URLInfo("/test/"), URLInfo("/123")), 10)
        val document = "Disallow: /test/ \n Disallow: /123 \n Crawl-delay: 10"

        val result = robotsParser.getRobotsData(document)
        Assertions.assertEquals(expectedResult, result)
    }
}