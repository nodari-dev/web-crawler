package parser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.urlParser.URLParser

class URLParserTest {
    private val urlParser = URLParser()

    @Test
    fun `returns filtered urls from html`() {
        val expectedResult = mutableListOf<String>("http://www.host.com", "http://www.host1.com")
        val html = "<div>" +
                "<a href='http://www.host.com'>link</a>" +
                "<a href='http://www.host1.com'>link</a>" +
                "</div>"

        val result = urlParser.getURLs(html)
        Assertions.assertEquals(result, expectedResult)
    }

    @Test
    fun `returns main url`() {
        val expectedResult = "https://test.com/"
        val document = "https://test.com/test/"

        val result = urlParser.getHostWithProtocol(document)
        Assertions.assertEquals(expectedResult, result)
    }
}