package application.parser.robotsparser

import application.interfaces.IRobotsParser
import application.parser.ParserUtils
import application.parser.robotsparser.RobotsPatterns.CRAWL_DELAY
import application.parser.robotsparser.RobotsPatterns.CRAWL_DELAY_GROUP_INDEX
import application.parser.robotsparser.RobotsPatterns.DISALLOW_KEYWORD
import application.parser.robotsparser.RobotsPatterns.DISALLOW_KEYWORD_GROUP_INDEX
import core.dto.RobotsData

class RobotsParser: IRobotsParser {
    private val parserUtils = ParserUtils()

    override fun getRobotsData(document: String): RobotsData {
        val urls = parserUtils.parseValues(document,
            DISALLOW_KEYWORD,
            DISALLOW_KEYWORD_GROUP_INDEX
        )
        val crawlDelay = parserUtils.parseSingleValue(document, CRAWL_DELAY, CRAWL_DELAY_GROUP_INDEX)?.toLong()
        return RobotsData(parserUtils.toURLInfo(urls), crawlDelay ?: -1)
    }
}