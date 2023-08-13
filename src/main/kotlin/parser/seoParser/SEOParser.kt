package parser.seoParser

import interfaces.ISEOParser
import parser.ParserUtils
import parser.seoParser.SEOPatterns.IMAGE_ALT
import parser.seoParser.SEOPatterns.IMAGE_ALT_GROUP_INDEX
import parser.seoParser.SEOPatterns.META_DESCRIPTION
import parser.seoParser.SEOPatterns.META_DESCRIPTION_GROUP_INDEX
import parser.seoParser.SEOPatterns.META_KEYWORDS
import parser.seoParser.SEOPatterns.META_KEYWORDS_GROUP_INDEX
import parser.seoParser.SEOPatterns.META_OG_DESCRIPTION
import parser.seoParser.SEOPatterns.META_OG_DESCRIPTION_GROUP_INDEX
import parser.seoParser.SEOPatterns.META_OG_TITLE
import parser.seoParser.SEOPatterns.META_OG_TITLE_GROUP_INDEX
import parser.seoParser.SEOPatterns.TITLE
import parser.seoParser.SEOPatterns.TITLE_GROUP_INDEX

class SEOParser: ISEOParser {
    private val parserUtils = ParserUtils()

    override fun getTitle(document: String): String? {
        return parserUtils.parseSingleValue(document, TITLE, TITLE_GROUP_INDEX)
    }

    override fun getMetaDescription(document: String): String? {
        return parserUtils.parseSingleValue(document, META_DESCRIPTION, META_DESCRIPTION_GROUP_INDEX)
    }

    override fun getOgTitle(document: String): String? {
        return parserUtils.parseSingleValue(document, META_OG_TITLE, META_OG_TITLE_GROUP_INDEX)
    }

    override fun getOMetaOgDescription(document: String): String? {
        return parserUtils.parseSingleValue(document, META_OG_DESCRIPTION, META_OG_DESCRIPTION_GROUP_INDEX)
    }

    override fun getImageAlts(document: String): List<String> {
        return parserUtils.parseValues(document, IMAGE_ALT, IMAGE_ALT_GROUP_INDEX)
    }

    override fun getMetaKeywords(document: String): List<String> {
        val keywords = parserUtils.parseValues(document, META_KEYWORDS, META_KEYWORDS_GROUP_INDEX)
        return if(keywords.isEmpty()) {
            emptyList()
        } else{
            keywords[0].split(",").map { it.trim() }
        }
    }
}