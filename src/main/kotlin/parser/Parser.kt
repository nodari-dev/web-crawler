package parser

import interfaces.IParser
import parser.RegexPatterns.A_TAG
import parser.RegexPatterns.A_TAG_GROUP_INDEX
import parser.RegexPatterns.META_KEYWORDS
import parser.RegexPatterns.META_KEYWORDS_GROUP_INDEX
import parser.RegexPatterns.UNSUPPORTED_FILETYPES

object Parser : IParser {
    override fun getFilteredURLs(html: String): List<String> {
        return parseAllHrefValues(html).filter { url -> !UNSUPPORTED_FILETYPES.matches(url) }
    }

    override fun getMetaKeywords(html: String): MutableList<String> {
        val metaKeywords = mutableListOf<String>()

        META_KEYWORDS.findAll(html).forEach { match ->
            metaKeywords.add(match.groups[META_KEYWORDS_GROUP_INDEX]!!.value)
        }

        return metaKeywords
    }

    private fun parseAllHrefValues(html: String): MutableList<String> {
        val childUrls = mutableListOf<String>()

        A_TAG.findAll(html).forEach { match ->
            childUrls.add(match.groups[A_TAG_GROUP_INDEX]!!.value)
        }

        return childUrls
    }
}