package fetcher

import crawler.Configuration
import java.io.IOException
import java.net.URL

class Fetcher {

    private val config = Configuration

    fun getHTML(url: String): String? {
        println("url to fetch $url")
        Thread.sleep(config.timeBetweenFetching)

        return try {
            URL(url).readText()
        } catch (e: IOException) {
            println("Could not parse document: $e")
            null
        }
    }

    fun getHEAD() {
        // to get data (last updated, etc...)
    }
}