package application.extractor

import application.interfaces.IDBExtractor
import core.dto.SEO
import core.dto.URLInfo

class DBExtractor: IDBExtractor {
    override fun execute(seo: SEO, urlInfo: URLInfo) {
        println("extract to db -> $seo")
    }
}