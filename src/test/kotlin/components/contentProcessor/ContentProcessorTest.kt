package components.contentProcessor

import core.configuration.Configuration.SAVE_FILE_LOCATION
import core.dto.WebLink
import core.dto.WebPage
import core.interfaces.components.IDataExtractor
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class ContentProcessorTest {

    private val dataExtractorMock = mock(IDataExtractor::class.java)

    private val contentProcessor = ContentProcessor(dataExtractorMock)

    @Test
    fun `processes html` (){
        val html = "<h1>Goodbye world</h1>"
        val webLink = WebLink("url")
        val webPage = WebPage(webLink, html)

        contentProcessor.processWebPage(webPage)

        verify(dataExtractorMock).extractSEODataToFile(webPage.html, webPage.link.url, SAVE_FILE_LOCATION)
    }
}