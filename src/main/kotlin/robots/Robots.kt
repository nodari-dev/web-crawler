package robots

import fetcher.Fetcher
import interfaces.IRobots
import parser.Parser

class Robots : IRobots {
    private val fetcher = Fetcher()

    override fun getDisallowedURLs(url: String): List<String> {
        val content = getRobotsTxtContent(url)
        return content?.let { Parser.getRobotsDisallowed(it) } ?: emptyList()
    }

    private fun getRobotsTxtContent(url: String): String? {
        val robotsURL = getRobotsURL(url)
        return fetcher.getPageContent(robotsURL)
    }

    private fun getRobotsURL(url: String): String {
        return Parser.getMainURL(url) + "robots.txt"
    }
}