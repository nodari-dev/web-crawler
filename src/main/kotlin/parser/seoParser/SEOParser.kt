package parser.seoParser

import interfaces.ISEOParser
import parser.ParserUtils

class SEOParser: ISEOParser {
    private val parserUtils = ParserUtils()

    override fun getTitle(document: String): String? {
        return parserUtils.parseSingleValue(document, SEOPatterns.TITLE, SEOPatterns.TITLE_GROUP_INDEX)
    }

    override fun getMetaDescription(document: String): String? {
        return parserUtils.parseSingleValue(document, SEOPatterns.META_DESCRIPTION, SEOPatterns.META_DESCRIPTION_GROUP_INDEX)
    }

    override fun getOgTitle(document: String): String? {
        return parserUtils.parseSingleValue(document, SEOPatterns.META_OG_TITLE, SEOPatterns.META_OG_TITLE_GROUP_INDEX)
    }

    override fun getOMetaOgDescription(document: String): String? {
        return parserUtils.parseSingleValue(document, SEOPatterns.META_OG_DESCRIPTION, SEOPatterns.META_OG_DESCRIPTION_GROUP_INDEX)
    }

    override fun getImageAlts(document: String): List<String> {
        return parserUtils.parseValues(document, SEOPatterns.IMAGE_ALT, SEOPatterns.IMAGE_ALT_GROUP_INDEX)
    }

    override fun getMetaKeywords(document: String): List<String> {
        val keywords = parserUtils.parseValues(document, SEOPatterns.META_KEYWORDS, SEOPatterns.META_KEYWORDS_GROUP_INDEX)
        return if(keywords.isEmpty()) {
            emptyList()
        } else{
            keywords[0].split(",").map { it.trim() }
        }
    }
}