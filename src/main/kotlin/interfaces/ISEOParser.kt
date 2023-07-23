package interfaces

interface ISEOParser {
    fun getTitle(document: String): String?
    fun getMetaDescription(document: String): String?
    fun getOgTitle(document: String): String?
    fun getOMetaOgDescription(document: String): String?
    fun getMetaKeywords(document: String): List<String>
    fun getImageAlts(document: String): List<String>
}