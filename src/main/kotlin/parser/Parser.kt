package parser

import interfaces.IParser
import parser.RegexPatterns.A_TAG
import parser.RegexPatterns.GROUP_INDEX
import parser.RegexPatterns.UNSUPPORTED_FILETYPES

object Parser: IParser {
    override fun getFilteredURLs(html: String): List<String> {
        return parseAllHrefValues(html).filter { url -> !UNSUPPORTED_FILETYPES.matches(url) }
    }

    private fun parseAllHrefValues(html: String): MutableList<String>{
        val childUrls = mutableListOf<String>()

        A_TAG.findAll(html).forEach { match ->
            childUrls.add(match.groups[GROUP_INDEX]!!.value)
        }

        return childUrls
    }
}