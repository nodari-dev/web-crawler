package parser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import parser.seoparser.SEOParser

class SEOParserTest {
    private val seoParser = SEOParser()

    @Test
    fun `returns meta tag keywords from html`() {
        val html = "<meta name=\"keywords\" content=\"keyword1, keyword2\">"
        val expectedResult = listOf("keyword1", "keyword2")
        val result = seoParser.getMetaKeywords(html)

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns images alt from html`() {
        val html = "<img src=\"asd\" alt=\"alt-test\"> <img src=\"asd\" alt=\"test-test\">"
        val expectedResult = mutableListOf("alt-test", "test-test")
        val result = seoParser.getImageAlts(html)

        Assertions.assertEquals(expectedResult, result)
    }
}