package components.parser.contentparser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ContentParserTest {
    private val contentParser = ContentParser()

    @Test
    fun `check if word is a common content`() {
        val common = "then"
        val uncommon = "Hello"

        Assertions.assertEquals(true, contentParser.isCommonContent(common))
        Assertions.assertEquals(false, contentParser.isCommonContent(uncommon))
    }
}