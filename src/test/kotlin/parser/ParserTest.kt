package parser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParserTest {

    @Test
    fun `returns filtered urls from html`() {
        val expectedResult = mutableListOf<String>("http://www.host.com", "http://www.host1.com")
        val html = "<div>" +
                "<a href='http://www.host.com'>link</a>" +
                "<a href='http://www.host1.com'>link</a>" +
                "</div>"

        val result = Parser.getFilteredURLs(html)
        Assertions.assertEquals(result, expectedResult)
    }
}