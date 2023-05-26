package fetcher

import crawler.Configuration
import org.jsoup.Connection
import org.jsoup.Jsoup

import java.io.IOException
import org.jsoup.HttpStatusException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class Fetcher {

    fun getPageContent(url: String): String? {
        var content: String? = null

        try {
            val connection = Jsoup.connect(url)
            connection.userAgent("Mozilla")
            connection.timeout(Configuration.timeBetweenFetching)
            content = toOneLineHTML(connection.get().toString())

        } catch (exception: HttpStatusException) {
            println(exception.statusCode)
        } catch (exception: UnknownHostException) {
            println(exception.message)
        } catch (exception: IllegalArgumentException) {
            println(exception.message)
        } catch (exception: SocketTimeoutException) {
            println(exception.message)
        } catch (exception: IOException) {
            println(exception.message)
        }

        return content
    }

    private fun toOneLineHTML(content: String): String{
        return content.replace("\n" , "")
    }
}