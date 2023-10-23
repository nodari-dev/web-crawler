package components.parser.robotsparser

import core.dto.HashedURLPair
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RobotsParserTest {
    private val robotsParser = RobotsParser()

    @Test
    fun `returns disallowed urls from robots txt`() {
        val expectedResult = mutableListOf(core.dto.HashedURLPair("/test/"), HashedURLPair("/123"))
        val document = "Disallow: /test/ \n Disallow: /123"

        val result = robotsParser.getRobotsDisallowed(document)
        Assertions.assertEquals(expectedResult, result)
    }
}