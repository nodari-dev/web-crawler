package interfaces

interface IKeywordGenerator {
    fun generateKeywords(sentences: List<String>): Map<String, Int>
}