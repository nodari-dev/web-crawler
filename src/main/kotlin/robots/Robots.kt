package robots

import dto.FormattedURL
import fetcher.Fetcher
import interfaces.IRobots
import parser.robotsParser.RobotsParser

class Robots : IRobots {
    private val fetcher = Fetcher()
    private val robotsParser = RobotsParser()

    override fun getDisallowedURLs(host: String, url: String): List<FormattedURL> {
        val content = getRobotsTxtContent(host, url)
        return content?.let { robotsParser.getRobotsDisallowed(it) } ?: emptyList()
    }

    private fun getRobotsTxtContent(host: String, url: String): String? {
        val robotsURL = "$host/robots.txt"
        return fetcher.getPageContent(robotsURL)
    }
}