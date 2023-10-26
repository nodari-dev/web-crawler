package components.interfaces

import core.dto.SEOContent

interface ISEODataAnalyzer {
    fun generateSEOData(html: String, url: String): SEOContent?
}