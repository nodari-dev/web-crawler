package application.interfaces

import core.dto.SEO
import core.dto.URLInfo

interface ISEOAnalyzer {
    fun generateSEO(html: String, urlInfo: URLInfo): SEO?
}