package storage.hosts

import dto.HashedURLPair
import fetcher.Fetcher
import interfaces.IRobotsUtils
import mu.KotlinLogging
import parser.robotsparser.RobotsParser

class RobotsUtils : IRobotsUtils {
    private val fetcher = Fetcher(KotlinLogging.logger("Fetсher"))
    private val robotsParser = RobotsParser()

    override fun getDisallowedURLs(host: String): List<HashedURLPair> {
        val document = getRobotsTxtDocument(host)
        return document?.let { content -> robotsParser.getRobotsDisallowed(content) } ?: emptyList()
    }

    private fun getRobotsTxtDocument(host: String): String? {
        val robotsURL = "$host/robots.txt"
        return fetcher.getPageHTML(robotsURL)
    }
}