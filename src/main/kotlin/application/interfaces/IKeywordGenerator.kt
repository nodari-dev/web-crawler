package application.interfaces

interface IKeywordGenerator {
    fun generateKeywords(sentences: List<String>): String
}