package application.fetcher

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import org.jsoup.Connection.Response
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import mu.KotlinLogging

import application.fetcher.exceptions.FetchingFailedException
import application.interfaces.IFetcher


class Fetcher: IFetcher {
    private val logger = KotlinLogging.logger("Fetcher")

    override fun downloadHTML(url: String): String? {
        val response = getResponse(url)
        return response?.let { response.parse().toString().replace("\n", "") }
    }

    private fun getResponse(url: String): Response? {
        return try {
            Jsoup.connect(url)
                .followRedirects(true)
                .userAgent("Mozilla/5.0 (compatible; CarrotBot/1)")
                .execute()
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