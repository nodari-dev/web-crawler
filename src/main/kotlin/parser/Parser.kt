package parser

import parser.RegexPatterns.A_TAG
import parser.RegexPatterns.GROUP_INDEX
import parser.RegexPatterns.UNSUPPORTED_FILETYPES

object Parser {
    fun getFilteredUrls(html: String): List<String> {
        println(getAllHrefValues(html))
        return getAllHrefValues(html).filter { url -> url.matches(UNSUPPORTED_FILETYPES) }
    }

    private fun getAllHrefValues(html: String): MutableList<String>{
        val childUrls = mutableListOf<String>()

        A_TAG.findAll(html).forEach { match ->
            childUrls.add(match.groups[GROUP_INDEX]!!.value)
        }

        return childUrls
    }
}