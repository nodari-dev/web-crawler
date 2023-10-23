package core.interfaces.components

import core.dto.SEOContent

interface ISEODataAnalyzer {
    fun generateSEOData(html: String, url: String): SEOContent?
}