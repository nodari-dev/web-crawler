package components.contentProcessor

import core.configuration.Configuration
import core.dto.WebPage
import core.interfaces.components.IContentProcessor
import core.interfaces.components.IDataExtractor

class ContentProcessor(val dataExtractor: IDataExtractor): IContentProcessor {

    override fun processWebPage(webPage: WebPage) {
        if(webPage.html == null){
            println("html is not correct")
        } else{
            dataExtractor.extractSEODataToFile(webPage.html, webPage.link.url, Configuration.SAVE_FILE_LOCATION)
        }
    }
}