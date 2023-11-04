package application.fetcher

import application.fetcher.exceptions.FetchingFailedException
import org.jsoup.Connection.Response
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import core.configuration.Configuration.TIME_BETWEEN_FETCHING
import application.interfaces.IFetcher
import mu.KLogger
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class Fetcher(private val logger: KLogger) : IFetcher {
    private val responseParser = ResponseParser()

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