package application.parser.seoparser

import application.interfaces.ISEOParser
import application.parser.ParserUtils
import application.parser.seoparser.SEOPatterns.IMAGE_ALT
import application.parser.seoparser.SEOPatterns.IMAGE_ALT_GROUP_INDEX
import application.parser.seoparser.SEOPatterns.META_DESCRIPTION
import application.parser.seoparser.SEOPatterns.META_DESCRIPTION_GROUP_INDEX
import application.parser.seoparser.SEOPatterns.META_KEYWORDS
import application.parser.seoparser.SEOPatterns.META_KEYWORDS_GROUP_INDEX
import application.parser.seoparser.SEOPatterns.META_OG_DESCRIPTION
import application.parser.seoparser.SEOPatterns.META_OG_DESCRIPTION_GROUP_INDEX
import application.parser.seoparser.SEOPatterns.META_OG_TITLE
import application.parser.seoparser.SEOPatterns.META_OG_TITLE_GROUP_INDEX
import application.parser.seoparser.SEOPatterns.PARAGRAPH
import application.parser.seoparser.SEOPatterns.PARAGRAPH_GROUP_INDEX
import application.parser.seoparser.SEOPatterns.TITLE
import application.parser.seoparser.SEOPatterns.TITLE_GROUP_INDEX
import application.parser.seoparser.SEOPatterns.HEADING
import application.parser.seoparser.SEOPatterns.HEADING_GROUP_INDEX

class SEOParser: ISEOParser {
    private val parserUtils = ParserUtils()

    override fun getTitle(document: String): String? {
        return parserUtils.parseSingleValue(document, TITLE, TITLE_GROUP_INDEX)
    }

    override fun getOgTitle(document: String): String? {
        return parserUtils.parseSingleValue(document, META_OG_TITLE, META_OG_TITLE_GROUP_INDEX)
    }

    override fun getMetaDescription(document: String): String? {
        return parserUtils.parseSingleValue(document, META_DESCRIPTION, META_DESCRIPTION_GROUP_INDEX)
    }

    override fun getOgMetaOgDescription(document: String): String? {
        return parserUtils.parseSingleValue(document, META_OG_DESCRIPTION, META_OG_DESCRIPTION_GROUP_INDEX)
    }

    override fun getMetaKeywords(document: String): List<String> {
        val keywords = parserUtils.parseValues(document, META_KEYWORDS, META_KEYWORDS_GROUP_INDEX)
        return if(keywords.isEmpty()) {
            emptyList()
        } else{
            keywords[0].split(",").map { it.trim() }
        }
    }

    override fun getHeadings(document: String): List<String>{
        return parserUtils.parseValues(document, HEADING, HEADING_GROUP_INDEX)
    }

    override fun getParagraphs(document: String): List<String>{
        return parserUtils.parseValues(document, PARAGRAPH, PARAGRAPH_GROUP_INDEX)
    }

    override fun getImageAlts(document: String): List<String> {
        return parserUtils.parseValues(document, IMAGE_ALT, IMAGE_ALT_GROUP_INDEX)
    }
}