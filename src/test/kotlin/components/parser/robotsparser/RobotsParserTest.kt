package components.parser.robotsparser

import application.parser.robotsparser.RobotsParser
import core.dto.URLInfo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RobotsParserTest {
    private val robotsParser = RobotsParser()

    @Test
    fun `returns disallowed urls from robots txt`() {
        val expectedResult = mutableListOf(core.dto.URLInfo("/test/"), URLInfo("/123"))
        val document = "Disallow: /test/ \n Disallow: /123"

        val result = robotsParser.getRobotsDisallowed(document)
        Assertions.assertEquals(expectedResult, result)
    }
}