package application.extractor

import application.interfaces.IExtractor
import configuration.Configuration.SAVE_FILE_LOCATION
import core.dto.SEO
import core.dto.URLInfo
import infrastructure.repository.SEORepository

class Extractor(SEORepository: SEORepository): IExtractor {
    private val fileExtractor = FileExtractor()
    private val dbExtractor = DBExtractor(SEORepository)

    override fun extractSEOData(seo: SEO, urlInfo: URLInfo){
        if(SAVE_FILE_LOCATION.isEmpty()){
            dbExtractor.execute(seo)
        } else{
            fileExtractor.execute(seo)
        }
    }
}