package parser

import parser.Parser
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

        val result = Parser.getURLs(html)
        Assertions.assertEquals(result, expectedResult)
    }

    @Test
    fun `returns meta tag keywords from html`() {
        val expectedResult = mutableListOf<String>("keyword1", "keyword2")
        val html = "<meta name=\"keywords\" content=\"keyword1, keyword2\">"

        val result = Parser.getMetaKeywords(html)
        Assertions.assertEquals(result, expectedResult)
    }

    @Test
    fun `returns meta robots from html`() {
        val expectedResult = mutableListOf<String>("keyword1", "keyword2")
        val html = "<meta name=\"keywords\" content=\"keyword1, keyword2\">"

        val result = Parser.getMetaKeywords(html)
        Assertions.assertEquals(expectedResult, result)
    }


    @Test
    fun `returns images alt from html`() {
        // TODO: FIX TEST
        val expectedResult = mutableListOf("alt-test", "test-test")
        val html = "<img src=\"asd\" alt=\"alt-test\"> <img src=\"asd\" alt=\"test-test\">"

        val result = Parser.getImagesAlt(html)
        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns disallowed urls from robots txt`() {
        val expectedResult = mutableListOf<String>("/test/", "/123")
        val document = "Disallow: /test/ \n Disallow: /123"

        val result = Parser.getRobotsDisallowed(document)
        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns main url`() {
        val expectedResult = "https://test.com/"
        val document = "https://test.com/test/"

        val result = Parser.getMainURL(document)
        Assertions.assertEquals(expectedResult, result)
    }
}