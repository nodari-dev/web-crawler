package parser.seoparser

import interfaces.ISEOParser
import parser.ParserUtils
import parser.seoparser.SEOPatterns.IMAGE_ALT
import parser.seoparser.SEOPatterns.IMAGE_ALT_GROUP_INDEX
import parser.seoparser.SEOPatterns.META_DESCRIPTION
import parser.seoparser.SEOPatterns.META_DESCRIPTION_GROUP_INDEX
import parser.seoparser.SEOPatterns.META_KEYWORDS
import parser.seoparser.SEOPatterns.META_KEYWORDS_GROUP_INDEX
import parser.seoparser.SEOPatterns.META_OG_DESCRIPTION
import parser.seoparser.SEOPatterns.META_OG_DESCRIPTION_GROUP_INDEX
import parser.seoparser.SEOPatterns.META_OG_TITLE
import parser.seoparser.SEOPatterns.META_OG_TITLE_GROUP_INDEX
import parser.seoparser.SEOPatterns.PARAGRAPH
import parser.seoparser.SEOPatterns.PARAGRAPH_GROUP_INDEX
import parser.seoparser.SEOPatterns.TITLE
import parser.seoparser.SEOPatterns.TITLE_GROUP_INDEX
import parser.seoparser.SEOPatterns.HEADING
import parser.seoparser.SEOPatterns.HEADING_GROUP_INDEX
import parser.GlobalPatterns.NESTED_TAGS

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

    override fun getHeadings(document: String): List<String>{
        return parserUtils.parseValues(document, NESTED_TAGS, HEADING, HEADING_GROUP_INDEX)
    }

    override fun getParagraphs(document: String): List<String>{
        return parserUtils.parseValues(document, NESTED_TAGS, PARAGRAPH, PARAGRAPH_GROUP_INDEX)
    }

    override fun getImageAlts(document: String): List<String> {
        return parserUtils.parseValues(document, NESTED_TAGS, IMAGE_ALT, IMAGE_ALT_GROUP_INDEX)
    }

    override fun getMetaKeywords(document: String): List<String> {
        val keywords = parserUtils.parseValues(document, NESTED_TAGS, META_KEYWORDS, META_KEYWORDS_GROUP_INDEX)
        return if(keywords.isEmpty()) {
            emptyList()
        } else{
            keywords[0].split(",").map { it.trim() }
        }
    }
}