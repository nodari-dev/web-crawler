package interfaces

interface IDataAnalyzer {
    fun getURLStats(url: String): String
    fun getPageStats(): String
}