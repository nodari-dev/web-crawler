package storage.hosts

import dto.HashedUrlPair
import fetcher.Fetcher
import interfaces.IRobotsUtils
import parser.robotsparser.RobotsParser

class RobotsUtils : IRobotsUtils {
    private val fetcher = Fetcher()
    private val robotsParser = RobotsParser()

    override fun getDisallowedURLs(host: String): List<HashedUrlPair> {
        val document = getRobotsTxtDocument(host)
        return document?.let { content -> robotsParser.getRobotsDisallowed(content) } ?: emptyList()
    }

    private fun getRobotsTxtDocument(host: String): String? {
        val robotsURL = "$host/robots.txt"
        return fetcher.getPageHTML(robotsURL)
    }
}