package core.interfaces.components

interface IDataExtractor {
    fun extractSEODataToFile(html: String, url: String, saveLocation: String)
}