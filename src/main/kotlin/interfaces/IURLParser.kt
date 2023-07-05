package interfaces

interface IURLParser {
    fun getURLs(document: String): List<String>
    fun getHostname(document: String): String
}