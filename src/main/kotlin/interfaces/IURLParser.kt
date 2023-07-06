package interfaces

interface IURLParser {
    fun getURLs(document: String): List<String>
    fun getMainURL(document: String): String
}