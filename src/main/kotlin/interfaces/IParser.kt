package interfaces

interface IParser {
    fun getFilteredURLs(html: String): List<String>
    fun getMetaKeywords(html: String): MutableList<String>
}