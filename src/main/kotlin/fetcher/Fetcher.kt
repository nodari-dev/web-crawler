package fetcher

import interfaces.IFetcher
import mu.KotlinLogging
import org.jsoup.Connection.Response
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import configuration.Configuration.TIME_BETWEEN_FETCHING
import exceptions.FetchingFailedException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class Fetcher : IFetcher {
    private val responseParser = ResponseParser()
    var logger = KotlinLogging.logger("Fetcher")

    override fun getPageHTML(url: String): String? {
        Thread.sleep(TIME_BETWEEN_FETCHING)
        val response = getResponse(url)
        return responseParser.parseDocument(response)
    }

    private fun getResponse(url: String): Response? {
        return try {
            logger.info("downloading $url")
            Jsoup.connect(url).followRedirects(true).execute()
        } catch (exception: Exception) {
            when (exception) {
                is HttpStatusException,
                is UnknownHostException,
                is IllegalArgumentException,
                is SocketTimeoutException,
                is FetchingFailedException -> {
                    logger.error ("Fetching of $url failed")
                    null
                }
                else -> {
                    logger.error ("Something went wrong with $url")
                    null
                }
            }
        }
    }
}