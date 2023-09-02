package parser.contentparser

import interfaces.IContentParser
import parser.ParserUtils
import parser.contentparser.ContentPatterns.COMMON_WORDS

class ContentParser: IContentParser {
    private val parserUtils = ParserUtils()

    override fun isCommonContent(content: String): Boolean {
        return parserUtils.isExisting(content, COMMON_WORDS)
    }
}