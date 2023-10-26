package components.htmlAnalyzer

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class KeywordGeneratorTest {
    private val keywordGenerator = KeywordGenerator()

    @Test
    fun `returns empty list is nothing found`() {
        val result = keywordGenerator.generateKeywords(emptyList())
        val expectedResult = emptyMap<String, Int>()
        Assertions.assertEquals(expectedResult, result)
    }

    @Test
    fun `returns keywords and sorts by rate according to provided text`() {
        val sentences = listOf(
            "To be or not to be      ",
            "That is a question."
        )

        val keywords = HashMap<String, Int>()
        keywords["be"] = 2
        keywords["question."] = 1
        keywords["that"] = 0
        keywords["a"] = 0
        keywords["not"] = 0
        keywords["or"] = 0
        keywords["is"] = 0
        keywords["to"] = 0
        val expectedResult = keywords.entries.sortedByDescending { keyword -> keyword.value }.associate { it.toPair() }
        val result = keywordGenerator.generateKeywords(sentences)
        Assertions.assertEquals(expectedResult, result)
    }
}