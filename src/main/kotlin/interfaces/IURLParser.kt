package interfaces

interface IURLParser {
    fun getURLs(document: String): List<String>
    fun getHostWithProtocol(document: String): String
}