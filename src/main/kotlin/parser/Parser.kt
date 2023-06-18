package parser

import interfaces.IParser
import parser.HTMLPatterns.A_TAG
import parser.HTMLPatterns.A_TAG_GROUP_INDEX
import parser.HTMLPatterns.IMAGE_ALT
import parser.HTMLPatterns.IMAGE_ALT_GROUP_INDEX
import parser.HTMLPatterns.META_KEYWORDS
import parser.HTMLPatterns.META_KEYWORDS_GROUP_INDEX
import parser.HTMLPatterns.META_ROBOTS_FOLLOW
import parser.HTMLPatterns.META_ROBOTS_FOLLOW_GROUP_INDEX
import parser.HTMLPatterns.UNSUPPORTED_FILETYPES
import parser.RobotsPatterns.DISALLOW_KEYWORD
import parser.RobotsPatterns.DISALLOW_KEYWORD_GROUP_INDEX
import parser.URLPatterns.MAIN_URL
import parser.URLPatterns.MAIN_URL_GROUP_INDEX

object Parser : IParser {
    override fun getURLs(document: String): List<String> {
        val urls = ParserUtils.parseValues(document, A_TAG, A_TAG_GROUP_INDEX)
        return urls.filter { url -> !UNSUPPORTED_FILETYPES.matches(url) }
    }

    override fun getImagesAlt(document: String): List<String> {
        return ParserUtils.parseValues(document, IMAGE_ALT, IMAGE_ALT_GROUP_INDEX)
    }

    override fun getRobotsDisallowed(document: String): List<String> {
        return ParserUtils.parseValues(document, DISALLOW_KEYWORD, DISALLOW_KEYWORD_GROUP_INDEX)
    }

    override fun getMetaKeywords(document: String): List<String> {
        return ParserUtils.parseValues(document, META_KEYWORDS, META_KEYWORDS_GROUP_INDEX)
    }

    override fun getMetaRobotsFollow(document: String): String? {
        return ParserUtils.parseSingleValue(document, META_ROBOTS_FOLLOW, META_ROBOTS_FOLLOW_GROUP_INDEX)
    }

    override fun getMainURL(document: String): String {
        return ParserUtils.parseSingleValue(document, MAIN_URL, MAIN_URL_GROUP_INDEX)!!
    }
}