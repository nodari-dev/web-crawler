package application.extractor

import application.htmlAnalyzer.HTMLAnalyzer
import application.extractor.exceptions.SaveFileException
import core.dto.SEOContent
import application.interfaces.IDataExtractor
import core.configuration.Configuration.SAVE_FILE_LOCATION
import java.io.File
import java.io.FileWriter

class Extractor : IDataExtractor {
    private val HTMLAnalyzer = HTMLAnalyzer()

    override fun extractSEODataToFile(html: String?, url: String) {
        val path = File(SAVE_FILE_LOCATION)
        if (!path.exists()) {
            path.mkdirs();
        }

        if(html != null){
            val seoData = HTMLAnalyzer.generateSEOData(html, url)
            if (seoData != null) {
                val fileName = generateFileName(url)
                val file = File("$SAVE_FILE_LOCATION/$fileName.txt")
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