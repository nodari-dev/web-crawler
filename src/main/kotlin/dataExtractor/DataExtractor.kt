package dataExtractor

import configuration.Configuration.SAVE_FILE_LOCATION
import dataExtractor.analyzer.SEODataAnalyzer
import parser.urlparser.URLParser
import java.io.File
import java.io.FileWriter
import java.io.IOException

class DataExtractor {
    private val seoDataAnalyzer = SEODataAnalyzer()
    private val urlParser = URLParser()

    fun extractSEODataToFile(html: String, url: String){
        val seoData = seoDataAnalyzer.generateSEOData(html, url)

        val urlWithoutProtocol = url.removeSuffix("/").removePrefix("http://").removePrefix("https://")
        val fileName = urlWithoutProtocol.replace("/", "_")

        if(seoData != null){
            val file = File("$SAVE_FILE_LOCATION/$fileName.txt")
            try {
                val content = "URL: ${seoData.url} ${seoData}, ${seoData.description} ${seoData.keywords}"
                FileWriter(file).use { fw ->
                    fw.write(content)
                    fw.flush()
                    fw.close()
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }
}