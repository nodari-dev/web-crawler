package application.parser.contentparser

import application.parser.ParserUtils
import application.parser.contentparser.ContentPatterns.COMMON_WORDS
import application.interfaces.IContentParser

class ContentParser: IContentParser {
    private val parserUtils = ParserUtils()

    override fun isCommonContent(content: String): Boolean {
        return parserUtils.isExisting(content, COMMON_WORDS)
    }
}