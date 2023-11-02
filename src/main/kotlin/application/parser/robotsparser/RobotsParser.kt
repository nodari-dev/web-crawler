package application.parser.robotsparser

import application.interfaces.IRobotsParser
import application.parser.ParserUtils
import application.parser.robotsparser.RobotsPatterns.DISALLOW_KEYWORD
import application.parser.robotsparser.RobotsPatterns.DISALLOW_KEYWORD_GROUP_INDEX

class RobotsParser: IRobotsParser {
    private val parserUtils = ParserUtils()

    override fun getRobotsDisallowed(document: String): List<core.dto.WebLink> {
        val urls = parserUtils.parseValues(document,
            DISALLOW_KEYWORD,
            DISALLOW_KEYWORD_GROUP_INDEX
        )
        return parserUtils.transformToFormattedURLs(urls)
    }
}