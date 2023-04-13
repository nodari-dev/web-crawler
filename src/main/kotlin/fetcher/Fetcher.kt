package fetcher

import Vertex
import crawler.Configuration
import java.io.IOException
import java.net.URL
import java.util.*

class Fetcher(private val config: Configuration) {

    fun getHTML(urlVertex: Vertex): String? {
        Thread.sleep(config.timeBetweenFetching)

        return try {
            URL(urlVertex.getUrl()).readText()
        } catch (e: IOException) {
            println("Could not parse document: $e")
            null
        }
    }

    fun getHEAD() {
        // to get data (last updated, etc...)
    }
}