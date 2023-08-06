package fetcher

import interfaces.IFetcher
import mu.KotlinLogging
import org.jsoup.Connection.Response
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import crawler.Counter
import controller.Configuration.TIME_BETWEEN_FETCHING
import exceptions.FetchingFailedException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class Fetcher : IFetcher {
    private val logger = KotlinLogging.logger("Fetcher")
    private val counter = Counter

    override fun getPageContent(url: String): String? {
        Thread.sleep(TIME_BETWEEN_FETCHING)
        return parseDocument(url)
    }

    private fun parseDocument(url: String): String? {
        val response = getResponse(url)
        return response?.let { toOneLineHTML(response.parse().toString()) }
    }

    private fun getResponse(url: String): Response? {
        return try {
            logger.info("[${counter.value}] downloaded $url")
            Jsoup.connect(url).followRedirects(true).execute()
        } catch (exception: Exception) {
            when (exception) {
                is HttpStatusException,
                is UnknownHostException,
                is IllegalArgumentException,
                is SocketTimeoutException,
                is FetchingFailedException -> {
                    logger.error { "Fetching of $url failed" }
                    null
                }
                else -> {
                    logger.error { "Something went wrong with $url" }
                    null
                }
            }
        }
    }

    private fun toOneLineHTML(content: String): String {
        return content.replace("\n", "")
    }
}