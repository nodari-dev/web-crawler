package fetcher

import crawler.Configuration.TIME_BETWEEN_FETCHING
import crawler.Configuration.CONNECTION_TIMEOUT
import interfaces.IFetcher
import org.jsoup.Jsoup

import java.io.IOException
import org.jsoup.HttpStatusException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class Fetcher: IFetcher {

    override fun getPageContent(url: String): String? {
        Thread.sleep(TIME_BETWEEN_FETCHING)
        
        var content: String? = null
        try {
            val connection = Jsoup.connect(url)
            connection.userAgent("Mozilla")
            connection.timeout(CONNECTION_TIMEOUT)
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