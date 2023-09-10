package interfaces

import dto.SEOContent

interface ISEODataAnalyzer {
    fun generateSEOData(html: String, url: String): SEOContent?
}