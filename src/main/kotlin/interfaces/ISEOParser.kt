package interfaces

interface ISEOParser {
    fun getMetaKeywords(document: String): List<String>
    fun getImageAlts(document: String): List<String>
}