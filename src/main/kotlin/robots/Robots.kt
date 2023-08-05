package robots

import dto.FormattedURL
import fetcher.Fetcher
import interfaces.IRobots
import parser.robotsParser.RobotsParser

class Robots : IRobots {
    private val fetcher = Fetcher()
    private val robotsParser = RobotsParser()

    override fun getDisallowedURLs(host: String): List<FormattedURL> {
        val document = getRobotsTxtDocument(host)
        return document?.let { content -> robotsParser.getRobotsDisallowed(content) } ?: emptyList()
    }

    private fun getRobotsTxtDocument(host: String): String? {
        val robotsURL = "$host/robots.txt"
        return fetcher.getPageContent(robotsURL)
    }
}