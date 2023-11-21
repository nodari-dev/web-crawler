package application.htmlAnalyzer

import application.extractor.MockedData
import application.htmlAnalyzer.HTMLAnalyzer
import core.dto.SEOContent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HTMLAnalyzerTest {
    private val HTMLAnalyzer = HTMLAnalyzer()

    @Test
    fun `generates SEOContent`() {

        val result = HTMLAnalyzer.generateSEOData(MockedData.html, MockedData.url)
        val expectedResult =
            SEOContent(MockedData.title, MockedData.metaDescription, MockedData.url, MockedData.getKeyWords())

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `generates SEOContent when title and description and uses OG`() {
        val result = HTMLAnalyzer.generateSEOData(MockedData.htmlOG, MockedData.url)
        val expectedResult =
            SEOContent(MockedData.OGtitle, null, MockedData.url, MockedData.getOgKeyWords())

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns null if keywords are empty`() {
        val result = HTMLAnalyzer.generateSEOData(
            MockedData.emptyHTML, "someurl.com"
        )
        Assertions.assertEquals(null, result)
    }
}