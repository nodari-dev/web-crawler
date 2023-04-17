package fetcher

import Node
import crawler.Configuration
import java.io.IOException
import java.net.URL

class Fetcher {

    private val config = Configuration

    fun getHTML(urlNode: Node): String? {
        Thread.sleep(config.timeBetweenFetching)

        return try {
            URL(urlNode.getUrl()).readText()
        } catch (e: IOException) {
            println("Could not parse document: $e")
            null
        }
    }

    fun getHEAD() {
        // to get data (last updated, etc...)
    }
}