package parser.seoParser

import interfaces.ISEOParser
import parser.ParserUtils
import parser.urlParser.URLPatterns

class SEOParser: ISEOParser {
    private val parserUtils = ParserUtils()

    override fun getImageAlts(document: String): List<String> {
        return parserUtils.parseValues(document, SEOPatterns.IMAGE_ALT, SEOPatterns.IMAGE_ALT_GROUP_INDEX)
    }

    override fun getMetaKeywords(document: String): List<String> {
        return parserUtils.parseValues(document, SEOPatterns.META_KEYWORDS, SEOPatterns.META_KEYWORDS_GROUP_INDEX)
    }
}