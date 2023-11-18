package components.parser

import application.parser.ParserUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.regex.Pattern

class ParserUtilsTest {
    private val parserUtils = ParserUtils()

    @Test
    fun `parses multiple values from document with removed nested tags`() {
        val html = """
            <html>
                <body>
                    <div><p>Item 1</p></div>
                    <div><p>Item 2</p></div>
                    <div>Item 3</div>
                </body>
            </html>
        """

        val pattern = Pattern.compile("""<div>(.*?)</div>""")
        val groupIndex = 1
        val expectedResult = listOf("Item 1", "Item 2", "Item 3")
        val result = parserUtils.parseValues(html, pattern, groupIndex)

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `parses single value from document and removes nested tags`() {
        val html = """
            <html>
                <body>
                    <p><strong>Hello world!</strong></p>
                </body>
            </html>
        """

        val pattern =  Pattern.compile("""<p>(.*?)</p>""")
        val groupIndex = 1
        val expectedResult = "Hello world!"
        val result = parserUtils.parseSingleValue(html, pattern, groupIndex)

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `parses and returns null in element is empty`() {
        val html = """
            <html>
                <body>
                    <p></p>
                </body>
            </html>
        """

        val pattern =  Pattern.compile("""<p>(.*?)</p>""")
        val groupIndex = 1
        val result = parserUtils.parseSingleValue(html, pattern, groupIndex)

        Assertions.assertEquals(null, result)
    }

    @Test
    fun `transforms list of string to list of FormattedURLs`(){
        val list = listOf("url1", "url2")
        val expectedResult = listOf(core.dto.URLInfo(list[0]), core.dto.URLInfo(list[1]))
        val result = parserUtils.transformToFormattedURLs(list)

        Assertions.assertEquals(expectedResult, result)
    }
}