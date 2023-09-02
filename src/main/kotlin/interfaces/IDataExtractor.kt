package interfaces

interface IDataExtractor {
    fun extractSEODataToFile(html: String, url: String, saveLocation: String)
}