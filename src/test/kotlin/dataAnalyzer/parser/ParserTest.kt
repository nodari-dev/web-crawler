package dataAnalyzer.parser

import dataAnalyzer.parser.Parser
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
        val expectedResult = mutableListOf<String>("title", "test")
//        val html = "<meta name=\"keywords\" content=\"keyword1, keyword2\">"
//
//        val result = Parser.getMetaKeywords(html)
//        Assertions.assertEquals(result, expectedResult)
    }


    @Test
    fun `returns image alt from html`() {
        val expectedResult = mutableListOf<String>("title", "test")
//        val html = "<meta name=\"keywords\" content=\"keyword1, keyword2\">"
//
//        val result = Parser.getMetaKeywords(html)
//        Assertions.assertEquals(result, expectedResult)
    }
}