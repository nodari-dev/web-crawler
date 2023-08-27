package parser.contentparser

import interfaces.IContentParser
import parser.ParserUtils
import parser.contentparser.ContentPatterns.COMMON_WORDS

class ContentParser: IContentParser {
    private val parserUtils = ParserUtils()

    override fun isCommonContent(html: String): Boolean {
        return parserUtils.isExisting(html, COMMON_WORDS)
    }
}