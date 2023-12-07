package application.htmlAnalyzer

import core.dto.SEO
import core.dto.URLInfo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SEODataAnalyzerTest {
    private val SEOAnalyzer = SEOAnalyzer()

    @Test
    fun `generates SEOContent`() {

        val result = SEOAnalyzer.generateSEO(MockedData.html, MockedData.url)
        val expectedResult =
            SEO(MockedData.title, MockedData.metaDescription, MockedData.url.link, "a,this,is,description,world!,imagealt,meta,example.,hello,introduction,example,simple,html")

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns null if keywords are empty`() {
        val result = SEOAnalyzer.generateSEO(
            MockedData.emptyHTML, URLInfo("someurl.com")
        )
        Assertions.assertEquals(null, result)
    }
}