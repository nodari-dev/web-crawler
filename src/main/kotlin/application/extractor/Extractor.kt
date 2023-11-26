package application.extractor

import application.interfaces.IExtractor
import configuration.Configuration
import core.dto.SEO
import core.dto.URLInfo

class Extractor: IExtractor {
    private val fileExtractor = FileExtractor()
    private val dbExtractor = DBExtractor()

    override fun extractSEOData(seo: SEO, urlInfo: URLInfo){
        if(Configuration.SAVE_FILE_LOCATION.isEmpty()){
            dbExtractor.execute(seo, urlInfo)
        } else{
            fileExtractor.execute(seo, urlInfo)
        }
    }
}