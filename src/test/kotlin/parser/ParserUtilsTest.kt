package parser

import dto.HashedUrlPair
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.regex.Pattern

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

        val pattern = Pattern.compile("""<div>(.*?)</div>""")
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

        val pattern =  Pattern.compile("""<p>(.*?)</p>""")
        val groupIndex = 1
        val expectedResult = "Hello world!"
        val result = parserUtils.parseSingleValue(html, pattern, groupIndex)

        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `transforms list of string to list of FormattedURLs`(){
        val list = listOf("url1", "url2")
        val expectedResult = listOf(HashedUrlPair(list[0]), HashedUrlPair(list[1]))
        val result = parserUtils.transformToFormattedURLs(list)

        Assertions.assertEquals(expectedResult, result)
    }
}