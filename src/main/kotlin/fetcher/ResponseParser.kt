package fetcher

import org.jsoup.Connection

class ResponseParser {

    fun parseDocument(response: Connection.Response?): String? {
        return response?.let { toOneLineHTML(response.parse().toString()) }
    }

    private fun toOneLineHTML(content: String): String {
        return content.replace("\n", "")
    }
}