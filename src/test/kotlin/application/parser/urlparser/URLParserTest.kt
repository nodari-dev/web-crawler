package application.parser.urlparser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import core.dto.URLInfo

class URLParserTest {
    private val urlParser = URLParser()

    @Test
    fun `returns filtered urls from html`() {
        val url1 = URLInfo("https://www.host.com")
        val url2 = URLInfo("https://www.host1.com")
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
    fun `returns host without protocol`() {
        val expectedResult = "test.com"
        val url = "https://test.com/test/"

        val result = urlParser.getHostname(url)
        Assertions.assertEquals(expectedResult, result)
    }
}