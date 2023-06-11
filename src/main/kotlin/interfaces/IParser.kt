package interfaces

interface IParser {
    fun getURLs(html: String): List<String>
    fun getMetaKeywords(html: String): List<String>
    fun getMetaRobotsFollow(html: String): String?
    fun getImageAlt(html: String): List<String>
}