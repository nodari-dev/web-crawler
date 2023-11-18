package application.interfaces

interface IDataExtractor {
    fun extractSEODataToFile(html: String?, url: String)
}