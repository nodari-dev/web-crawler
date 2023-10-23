package components.parser.robotsparser

import core.interfaces.components.IRobotsParser
import components.parser.ParserUtils
import components.parser.robotsparser.RobotsPatterns.DISALLOW_KEYWORD
import components.parser.robotsparser.RobotsPatterns.DISALLOW_KEYWORD_GROUP_INDEX

class RobotsParser: IRobotsParser {
    private val parserUtils = ParserUtils()

    override fun getRobotsDisallowed(document: String): List<core.dto.HashedURLPair> {
        val urls = parserUtils.parseValues(document,
            DISALLOW_KEYWORD,
            DISALLOW_KEYWORD_GROUP_INDEX
        )
        return parserUtils.transformToFormattedURLs(urls)
    }
}