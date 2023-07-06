package parser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ParserUtilsTest {
    private val parserUtils = ParserUtils()

    @Test
    fun `parses multiple values from document`() {
        val html = """
            <html>
                <body>
                    <div>Item 1</div>
                    <div>Item 2</div>
                    <div>Item 3</div>
                </body>
            </html>
        """

        val pattern = """<div>(.*?)</div>""".toRegex()
        val groupIndex = 1
        val expectedResult = listOf("Item 1", "Item 2", "Item 3")
        val result = parserUtils.parseValues(html, pattern, groupIndex)

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `parses single value from document`() {
        val html = """
            <html>
                <body>
                    <p>Hello world!</p>
                </body>
            </html>
        """

        val pattern = """<p>(.*?)</p>""".toRegex()
        val groupIndex = 0
        val expectedResult = "Hello world!"
        val result = parserUtils.parseSingleValue(html, pattern, groupIndex)

        Assertions.assertEquals(expectedResult, result)
    }
}