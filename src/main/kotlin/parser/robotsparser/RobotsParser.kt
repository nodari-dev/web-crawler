package parser.robotsparser

import dto.FormattedURL
import interfaces.IRobotsParser
import parser.ParserUtils
import parser.robotsparser.RobotsPatterns.DISALLOW_KEYWORD
import parser.robotsparser.RobotsPatterns.DISALLOW_KEYWORD_GROUP_INDEX

class RobotsParser: IRobotsParser {
    private val parserUtils = ParserUtils()

    override fun getRobotsDisallowed(document: String): List<FormattedURL> {
        val urls = parserUtils.parseValues(document,
            DISALLOW_KEYWORD,
            DISALLOW_KEYWORD_GROUP_INDEX
        )
        return parserUtils.transformToFormattedURLs(urls)
    }
}