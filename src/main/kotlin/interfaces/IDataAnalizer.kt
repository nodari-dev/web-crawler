package interfaces

interface IDataAnalyzer {
    fun getPageStats(html: String): String
}