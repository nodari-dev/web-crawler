package analyzer

import dataExtractor.analyzer.SEODataAnalyzer
import dto.SEOContent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SEODataAnalyzerTest {
    private val seoDataAnalyzer = SEODataAnalyzer()

    @Test
    fun `generates SEOContent`() {

        val result = seoDataAnalyzer.generateSEOData(MockedData.html, MockedData.url)
        val expectedResult =
            SEOContent(MockedData.title, MockedData.metaDescription, MockedData.url, MockedData.getKeyWords())

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `generates SEOContent when title and description and uses OG`() {
        val result = seoDataAnalyzer.generateSEOData(MockedData.htmlOG, MockedData.url)
        val expectedResult =
            SEOContent(MockedData.OGtitle, null, MockedData.url, MockedData.getOgKeyWords())

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns null if keywords are empty`() {
        val result = seoDataAnalyzer.generateSEOData(
            MockedData.emptyHTML, "someurl.com"
        )
        Assertions.assertEquals(null, result)
    }
}