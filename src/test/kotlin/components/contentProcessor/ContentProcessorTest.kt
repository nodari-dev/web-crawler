package components.contentProcessor

import core.configuration.Configuration.SAVE_FILE_LOCATION
import core.dto.WebLink
import core.dto.WebPage
import application.interfaces.IDataExtractor
import application.interfaces.IFrontier
import components.interfaces.IURLParser
import application.interfaces.IURLValidator
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class ContentProcessorTest {

    private val dataExtractorMock = mock(IDataExtractor::class.java)
    private val urlParserMock = mock(IURLParser::class.java)
    private val frontierMock = mock(IFrontier::class.java)
    private val urlValidator = mock(IURLValidator::class.java)

    private val contentProcessor = ContentProcessor(
        dataExtractorMock,
        frontierMock,
        urlValidator
    )

    @Test
    fun `processes html` (){
        val host = "https://website.com"
        val link = "$host/something"
        val html = "<h1>Goodbye world</h1><a href='$link'/>"
        val webLink = WebLink(link)
        val webPage = WebPage(webLink, html)

        contentProcessor.processWebPage(webPage)

        `when`(urlValidator.canProcessURL(host, webLink)).thenReturn(true)
        verify(frontierMock).updateOrCreateQueue(host, webLink.url)

        verify(dataExtractorMock).extractSEODataToFile(webPage.html, webPage.link.url, SAVE_FILE_LOCATION)
    }
}