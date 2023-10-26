package components.parser.contentparser

import components.parser.ParserUtils
import components.parser.contentparser.ContentPatterns.COMMON_WORDS
import components.interfaces.IContentParser

class ContentParser: IContentParser {
    private val parserUtils = ParserUtils()

    override fun isCommonContent(content: String): Boolean {
        return parserUtils.isExisting(content, COMMON_WORDS)
    }
}