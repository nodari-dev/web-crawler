package application.interfaces

import core.dto.SEO
import core.dto.URLInfo

interface IDBExtractor {
    fun execute(seo: SEO, urlInfo: URLInfo)
}