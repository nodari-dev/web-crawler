package core.interfaces.components

interface IKeywordGenerator {
    fun generateKeywords(sentences: List<String>): Map<String, Int>
}