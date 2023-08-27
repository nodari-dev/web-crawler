package interfaces

interface IContentParser {
    fun isCommonContent(html: String): Boolean
}