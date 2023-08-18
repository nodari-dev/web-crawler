package analyzer

import dto.SEOContent
import interfaces.IDataAnalyzer
import parser.seoparser.SEOParser

class DataAnalyzer : IDataAnalyzer {
    private val seoParser = SEOParser()

    override fun getPageStats(html: String): SEOContent {
        return SEOContent(
            seoParser.getTitle(html),
            seoParser.getOgTitle(html),
            seoParser.getMetaDescription(html),
            seoParser.getOMetaOgDescription(html),
            seoParser.getMetaKeywords(html),
            seoParser.getImageAlts(html)
        )
    }
}