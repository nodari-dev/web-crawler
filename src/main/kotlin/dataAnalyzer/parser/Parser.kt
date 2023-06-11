package dataAnalyzer.parser

import interfaces.IParser
import dataAnalyzer.parser.RegexPatterns.A_TAG
import dataAnalyzer.parser.RegexPatterns.A_TAG_GROUP_INDEX
import dataAnalyzer.parser.RegexPatterns.IMAGE_ALT
import dataAnalyzer.parser.RegexPatterns.IMAGE_ALT_GROUP_INDEX
import dataAnalyzer.parser.RegexPatterns.META_KEYWORDS
import dataAnalyzer.parser.RegexPatterns.META_KEYWORDS_GROUP_INDEX
import dataAnalyzer.parser.RegexPatterns.META_ROBOTS_FOLLOW
import dataAnalyzer.parser.RegexPatterns.META_ROBOTS_FOLLOW_GROUP_INDEX
import dataAnalyzer.parser.RegexPatterns.UNSUPPORTED_FILETYPES

object Parser : IParser {
    override fun getURLs(html: String): List<String> {
        val urls = ParserUtils.parseValues(html, A_TAG, A_TAG_GROUP_INDEX)
        return urls.filter { url -> !UNSUPPORTED_FILETYPES.matches(url) }
    }

    override fun getImageAlt(html: String): List<String> {
        return ParserUtils.parseValues(html, IMAGE_ALT, IMAGE_ALT_GROUP_INDEX)
    }

    override fun getMetaRobotsFollow(html: String): String? {
        return ParserUtils.parseSingleValue(html, META_ROBOTS_FOLLOW, META_ROBOTS_FOLLOW_GROUP_INDEX)
    }

    override fun getMetaKeywords(html: String): List<String> {
        return ParserUtils.parseValues(html, META_KEYWORDS, META_KEYWORDS_GROUP_INDEX)
    }
}