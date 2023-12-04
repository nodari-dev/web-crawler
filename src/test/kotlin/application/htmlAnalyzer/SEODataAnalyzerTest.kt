package application.htmlAnalyzer

import application.extractor.MockedData
import core.dto.SEO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SEODataAnalyzerTest {
    private val SEOAnalyzer = SEOAnalyzer()
//
//    @Test
//    fun `generates SEOContent`() {
//
//        val result = SEOAnalyzer.generateSEO(MockedData.html, MockedData.url)
//        val expectedResult =
//            SEO(MockedData.title, MockedData.metaDescription, MockedData.url, MockedData.getKeyWords())
//
//        Assertions.assertEquals(expectedResult, result)
//    }
//
//    @Test
//    fun `generates SEOContent when title and description and uses OG`() {
//        val result = SEOAnalyzer.generateSEO(MockedData.htmlOG, MockedData.url)
//        val expectedResult =
//            SEO(MockedData.OGtitle, null, MockedData.url, MockedData.getOgKeyWords())
//
//        Assertions.assertEquals(expectedResult, result)
//    }
//
//    @Test
//    fun `returns null if keywords are empty`() {
//        val result = SEOAnalyzer.generateSEO(
//            MockedData.emptyHTML, "someurl.com"
//        )
//        Assertions.assertEquals(null, result)
//    }
}