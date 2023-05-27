package interfaces

interface IParser {
    fun getFilteredURLs(html: String): List<String>
}