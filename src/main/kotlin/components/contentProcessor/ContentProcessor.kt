package components.contentProcessor

import application.interfaces.IDataExtractor
import storage.interfaces.IFrontier
import application.interfaces.IURLValidator
import components.interfaces.IContentProcessor
import components.parser.urlparser.URLParser
import core.configuration.Configuration
import core.dto.WebLink
import core.dto.WebPage

class ContentProcessor(
    private val dataExtractor: IDataExtractor,
    private val frontier: IFrontier,
    private val urlValidator: IURLValidator
): IContentProcessor {
    private val urlParser = URLParser()

    override fun processWebPage(webPage: WebPage) {
        if(webPage.html == null){
            println("html is not correct")
        } else{
            dataExtractor.extractSEODataToFile(webPage.html, webPage.link.url, Configuration.SAVE_FILE_LOCATION)
            processChildURLs(urlParser.getURLs(webPage.html))
        }
    }

    private fun processChildURLs(urls: List<WebLink>) {
        val uniqueHashedUrlPairs = urls.toSet()
        uniqueHashedUrlPairs.forEach { hashedUrlPair ->
            val host = urlParser.getHostWithProtocol(hashedUrlPair.url)
            if (urlValidator.canProcessURL(host, hashedUrlPair)) {
                frontier.updateOrCreateQueue(host, hashedUrlPair.url)
            }
        }
    }
}