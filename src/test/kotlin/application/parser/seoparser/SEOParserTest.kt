package application.parser.seoparser

import application.parser.seoparser.SEOParser
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SEOParserTest {
    private val seoParser = SEOParser()

    @Test
    fun `returns title and ogTitle from html`() {
        val html = "<title>title</title>"
        val htmlOG = """<meta property="og:title" content="This is a basic text">"""
        Assertions.assertEquals("title", seoParser.getTitle(html))
        Assertions.assertEquals("This is a basic text", seoParser.getOgTitle(htmlOG))
    }

    @Test
    fun `return meta or ogMeta description from html`() {
        val html = """<meta name="description" content="not og">"""
        val htmlOG = """<meta property="og:description" content="og">"""
        Assertions.assertEquals("not og", seoParser.getMetaDescription(html))
        Assertions.assertEquals("og", seoParser.getOgMetaOgDescription(htmlOG))
    }

    @Test
    fun `returns meta tag keywords from html`() {
        val html = "<meta name=\"keywords\" content=\"keyword1, keyword2\">"
        val expectedResult = listOf("keyword1", "keyword2")
        val result = seoParser.getMetaKeywords(html)

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns headings from html`() {
        val html = "<h1>h1-content</h1> <h6>h6-content</h6>"
        val expectedResult = mutableListOf("h1-content", "h6-content")
        val result = seoParser.getHeadings(html)

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns paragraphs from html`() {
        val html = "<p>paragraph-1</p> <p>paragraph-2</p>"
        val expectedResult = mutableListOf("paragraph-1", "paragraph-2")
        val result = seoParser.getParagraphs(html)
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