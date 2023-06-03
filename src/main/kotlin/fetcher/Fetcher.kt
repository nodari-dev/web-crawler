package fetcher

import crawler.Configuration.TIME_BETWEEN_FETCHING
import interfaces.IFetcher
import org.jsoup.Connection.Response
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object Fetcher : IFetcher {

    override fun getPageContent(url: String): String? {
        Thread.sleep(TIME_BETWEEN_FETCHING)
        return parseDocument(url)
    }

    private fun parseDocument(url: String): String? {
        val response = getResponse(url)
        return response?.let { toOneLineHTML(response.parse().toString()) }
    }

    private fun getResponse(url: String): Response? {
        var response: Response? = null
        try {
            response = Jsoup.connect(url).followRedirects(true).execute()
        } catch (exception: Exception) {
            when (exception) {
                is HttpStatusException,
                is UnknownHostException,
                is IllegalArgumentException,
                is SocketTimeoutException,
                is IOException -> {
                    println(exception.message)
                }
            }
        }

        return response
    }

    private fun toOneLineHTML(content: String): String {
        return content.replace("\n", "")
    }
}