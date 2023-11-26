package application.interfaces

import core.dto.SEO
import core.dto.URLInfo

interface IExtractor {
    fun extractSEOData(seo: SEO, urlInfo: URLInfo)
}