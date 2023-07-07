package robots

import fetcher.Fetcher
import interfaces.IRobots
import parser.robotsParser.RobotsParser
import parser.urlParser.URLParser
import java.net.URL

class Robots : IRobots {
    private val fetcher = Fetcher()
    private val robotsParser = RobotsParser()
    private val urlParser = URLParser()

    override fun getDisallowedURLs(url: String): List<String> {
        val content = getRobotsTxtContent(url)
        return content?.let { robotsParser.getRobotsDisallowed(it) } ?: emptyList()
    }

    private fun getRobotsTxtContent(url: String): String? {
        val robotsURL = getRobotsURL(url)
        return fetcher.getPageContent(robotsURL)
    }

    private fun getRobotsURL(url: String): String {
        val host = urlParser.getHostWithProtocol(url).host
        return "$host/robots.txt"
    }
}