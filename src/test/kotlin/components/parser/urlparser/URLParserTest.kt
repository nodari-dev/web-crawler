package components.parser.urlparser

import application.parser.urlparser.URLParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class URLParserTest {
    private val urlParser = URLParser()

    @Test
    fun `returns filtered urls from html`() {
        val url1 = core.dto.URLData("https://www.host.com")
        val url2 = core.dto.URLData("https://www.host1.com")
        val expectedResult = listOf(url1, url2)
        val html = "<div>" +
                "<a href='https://www.host.com'>link</a>" +
                "<a href='https://www.host1.com'>link</a>" +
                "<a href='https://www.host2.com/file.cpp'>link</a>" +
                "</div>"

        val result = urlParser.getURLs(html)
        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns host with protocol`() {
        val expectedResult = "https://test.com"
        val url = "https://test.com/test/"

        val result = urlParser.getHostWithProtocol(url)
        Assertions.assertEquals(expectedResult, result)
    }
}