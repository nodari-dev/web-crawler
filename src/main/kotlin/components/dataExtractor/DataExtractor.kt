package components.dataExtractor

import components.contentAnalyzer.ContentAnalyzer
import components.dataExtractor.exceptions.SaveFileException
import core.dto.SEOContent
import core.interfaces.components.IDataExtractor
import java.io.File
import java.io.FileWriter

class DataExtractor : IDataExtractor {
    private val contentAnalyzer = ContentAnalyzer()

    override fun extractSEODataToFile(html: String, url: String, saveLocation: String) {
        val path = File(saveLocation)
        if (!path.exists()) {
            path.mkdirs();
        }

        val seoData = contentAnalyzer.generateSEOData(html, url)
        if (seoData != null) {
            val fileName = generateFileName(url)
            val file = File("$saveLocation/$fileName.txt")
            val fileContent = generateFileContent(seoData)
            try {
                FileWriter(file).use { fw ->
                    fw.write(fileContent)
                    fw.flush()
                    fw.close()
                }
            } catch (ex: SaveFileException) {
                ex.printStackTrace()
            }
        }
    }

    private fun generateFileName(url: String): String {
        return url.removeSuffix("/")
            .removePrefix("http://")
            .removePrefix("https://")
            .replace("/", "_")
    }

    private fun generateFileContent(seoData: SEOContent): String {
        return "URL: ${seoData.url}\nTitle: ${seoData.title}\nDescription: ${seoData.description}\nKeywords: ${seoData.keywords}"
    }
}