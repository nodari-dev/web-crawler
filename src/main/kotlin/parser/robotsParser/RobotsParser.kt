package parser.robotsParser

import dto.FormattedURL
import interfaces.IRobotsParser
import parser.ParserUtils

class RobotsParser: IRobotsParser {
    private val parserUtils = ParserUtils()

    override fun getRobotsDisallowed(document: String): List<FormattedURL> {
        val urls = parserUtils.parseValues(document,
            RobotsPatterns.DISALLOW_KEYWORD,
            RobotsPatterns.DISALLOW_KEYWORD_GROUP_INDEX
        )
        return parserUtils.transformToFormattedURLs(urls)
    }
}