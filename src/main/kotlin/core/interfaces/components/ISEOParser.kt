package core.interfaces.components

interface ISEOParser {
    fun getTitle(document: String): String?
    fun getOgTitle(document: String): String?
    fun getMetaDescription(document: String): String?
    fun getOgMetaOgDescription(document: String): String?
    fun getMetaKeywords(document: String): List<String>
    fun getHeadings(document: String): List<String>
    fun getParagraphs(document: String): List<String>
    fun getImageAlts(document: String): List<String>
}