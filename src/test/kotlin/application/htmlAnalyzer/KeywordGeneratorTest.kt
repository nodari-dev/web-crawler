package application.htmlAnalyzer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KeywordGeneratorTest {
    private val keywordGenerator = KeywordGenerator()

    @Test
    fun `returns empty list is nothing found`() {
        val result = keywordGenerator.generateKeywords(emptyList())
        val expectedResult = ""
        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns keywords and sorts by rate according to provided text`() {
        val sentences = listOf(
            "To be or not to be      ",
            "That is a question."
        )

        val result = keywordGenerator.generateKeywords(sentences)
        Assertions.assertEquals("that,a,not,or,is,to,question.,be", result)
    }
}