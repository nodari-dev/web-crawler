package dataExtractor.analyzer

import contentAnalyzer.ContentAnalyzer
import dataExtractor.MockedData
import dto.SEOContent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ContentAnalyzerTest {
    private val contentAnalyzer = ContentAnalyzer()

    @Test
    fun `generates SEOContent`() {

        val result = contentAnalyzer.generateSEOData(MockedData.html, MockedData.url)
        val expectedResult =
            SEOContent(MockedData.title, MockedData.metaDescription, MockedData.url, MockedData.getKeyWords())

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `generates SEOContent when title and description and uses OG`() {
        val result = contentAnalyzer.generateSEOData(MockedData.htmlOG, MockedData.url)
        val expectedResult =
            SEOContent(MockedData.OGtitle, null, MockedData.url, MockedData.getOgKeyWords())

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns null if keywords are empty`() {
        val result = contentAnalyzer.generateSEOData(
            MockedData.emptyHTML, "someurl.com"
        )
        Assertions.assertEquals(null, result)
    }
}