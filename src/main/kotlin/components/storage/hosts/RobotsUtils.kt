package components.storage.hosts

import components.fetcher.Fetcher
import components.parser.robotsparser.RobotsParser
import core.interfaces.components.IRobotsUtils
import mu.KotlinLogging

class RobotsUtils : IRobotsUtils {
    private val fetcher = Fetcher(KotlinLogging.logger("Fetсher"))
    private val robotsParser = RobotsParser()

    override fun getDisallowedURLs(host: String): List<core.dto.HashedURLPair> {
        val document = getRobotsTxtDocument(host)
        return document?.let { content -> robotsParser.getRobotsDisallowed(content) } ?: emptyList()
    }

    private fun getRobotsTxtDocument(host: String): String? {
        val robotsURL = "$host/robots.txt"
        return fetcher.getPageHTML(robotsURL)
    }
}