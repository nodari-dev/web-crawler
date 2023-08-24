package parser.robotsparser

import dto.FormattedURL
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.robotsparser.RobotsParser

class RobotsParserTest {
    private val robotsParser = RobotsParser()

    @Test
    fun `returns disallowed urls from robots txt`() {
        val expectedResult = mutableListOf(FormattedURL("/test/"), FormattedURL("/123"))
        val document = "Disallow: /test/ \n Disallow: /123"

        val result = robotsParser.getRobotsDisallowed(document)
        Assertions.assertEquals(expectedResult, result)
    }
}