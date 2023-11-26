package application.extractor

import application.extractor.exceptions.SaveFileException
import core.dto.SEO
import application.interfaces.IDBExtractor
import configuration.Configuration.SAVE_FILE_LOCATION
import core.dto.URLInfo
import java.io.File
import java.io.FileWriter

class FileExtractor : IDBExtractor {

    override fun execute(seo: SEO, urlInfo: URLInfo) {
        val path = File(SAVE_FILE_LOCATION)
        if (!path.exists()) {
            path.mkdirs();
        }

        val fileName = generateFileName(urlInfo.link)
        val file = File("$SAVE_FILE_LOCATION/$fileName.txt")
        val fileContent = generateFileContent(seo)
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

    private fun generateFileName(url: String): String {
        return url.removeSuffix("/")
            .removePrefix("http://")
            .removePrefix("https://")
            .replace("/", "_")
    }

    private fun generateFileContent(seoData: SEO): String {
        return "URL: ${seoData.url}\nTitle: ${seoData.title}\nDescription: ${seoData.description}\nKeywords: ${seoData.keywords}"
    }
}