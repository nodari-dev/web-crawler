package dataExtractor

import configuration.Configuration.SAVE_FILE_LOCATION
import dataExtractor.analyzer.SEODataAnalyzer
import dto.SEOContent
import interfaces.IDataExtractor
import java.io.File
import java.io.FileWriter
import java.io.IOException

class DataExtractor: IDataExtractor {
    private val seoDataAnalyzer = SEODataAnalyzer()

    override fun extractSEODataToFile(html: String, url: String) {
        val seoData = seoDataAnalyzer.generateSEOData(html, url)
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
            } catch (ex: IOException) {
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