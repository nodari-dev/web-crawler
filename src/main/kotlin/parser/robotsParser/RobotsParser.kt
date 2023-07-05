package parser.robotsParser

import interfaces.IRobotsParser
import parser.ParserUtils

class RobotsParser: IRobotsParser {
    private val parserUtils = ParserUtils()

    override fun getRobotsDisallowed(document: String): List<String> {
        return parserUtils.parseValues(document,
            RobotsPatterns.DISALLOW_KEYWORD,
            RobotsPatterns.DISALLOW_KEYWORD_GROUP_INDEX
        )
    }
}