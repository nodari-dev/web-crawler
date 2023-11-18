package storage.hosts

import application.fetcher.Fetcher
import application.parser.robotsparser.RobotsParser
import core.dto.URLInfo
import application.interfaces.IRobotsUtils
import mu.KotlinLogging

class RobotsUtils : IRobotsUtils {
    private val fetcher = Fetcher(KotlinLogging.logger("Fetсher"))
    private val robotsParser = RobotsParser()

    override fun getDisallowedURLs(host: String): List<URLInfo> {
        val document = getRobotsTxtDocument(host)
        return document?.let { content -> robotsParser.getRobotsDisallowed(content) } ?: emptyList()
    }

    private fun getRobotsTxtDocument(host: String): String? {
        val robotsURL = "$host/robots.txt"
        return fetcher.getPageHTML(robotsURL)
    }
}