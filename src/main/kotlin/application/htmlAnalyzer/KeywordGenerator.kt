package application.htmlAnalyzer

import application.parser.contentparser.ContentParser
import application.interfaces.IKeywordGenerator
import mu.KotlinLogging

class KeywordGenerator : IKeywordGenerator {
    private val contentParser = ContentParser()
    private val logger = KotlinLogging.logger("KeywordGenerator")

    override fun generateKeywords(sentences: List<String>): Map<String, Int> {
        return when (sentences.isEmpty()) {
            true -> processEmptySentences()
            else -> processSentences(sentences)
        }
    }

    private fun processEmptySentences(): Map<String, Int> {
        logger.error("no content to generate keywords from")
        return emptyMap()
    }

    private fun processSentences(sentences: List<String>): Map<String, Int> {
        val keywords = HashMap<String, Int>()
        for (sentence in sentences) {
            val words = sentence.split(" ")
            for (word in words) {
                if (canProcessWord(word)) {
                    val formattedWord = word.trim().lowercase()
                    keywords[formattedWord] =
                        keywords.getOrDefault(formattedWord, 0) + generatePriorityNumber(formattedWord)
                }
            }
        }

        return getSortedKeywordsByFrequency(keywords)
    }

    private fun canProcessWord(word: String): Boolean {
        return word.isNotBlank() || word.isNotEmpty()
    }

    private fun generatePriorityNumber(word: String): Int {
        return if (contentParser.isCommonContent(word)) 0 else 1
    }

    private fun getSortedKeywordsByFrequency(keywords: Map<String, Int>): Map<String, Int> {
        return keywords.entries.sortedByDescending { keyword -> keyword.value }.associate { it.toPair() }
    }
}