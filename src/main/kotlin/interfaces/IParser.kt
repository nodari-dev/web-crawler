package interfaces

interface IParser {
    fun getURLs(document: String): List<String>
    fun getMetaKeywords(document: String): List<String>
    fun getImagesAlt(document: String): List<String>
    fun getRobotsDisallowed(document: String): List<String>
    fun getMetaRobotsFollow(document: String): String?
    fun getMainURL(document: String): String

}