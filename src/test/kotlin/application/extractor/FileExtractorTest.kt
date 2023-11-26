package application.extractor

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class FileExtractorTest {
    private val fileExtractor = FileExtractor()

    private val url = "https://website.com/something"
    private val fileName = "website.com_something.txt"

    private val filePath = Paths.get(fileName);

    @AfterEach
    fun `delete temp files`() {
        if(Files.exists(filePath)){
            Files.delete(filePath)
        }
    }

    @Test
    fun `extracts SEOData to file in txt format`() {
        val expectedResult = """
            URL: https://website.com/something
            Title: Simple HTML Example
            Description: Meta description
            Keywords: {simple=3, html=3, example=2, description=1, world!=1, imagealt=1, meta=1, example.=1, hello=1, introduction=1, a=0, this=0, is=0}
        """.trimIndent()

        fileExtractor.extractSEOData(MockedData.html, url)

        val content = Files.readAllBytes(filePath)
        val fileContent = String(content)
        Assertions.assertEquals(expectedResult, fileContent)
    }

    @Test
    fun `does not generate file if SEOData is null`() {
        fileExtractor.extractSEOData("", url)
        Assertions.assertEquals(Files.exists(filePath), false)
    }
}