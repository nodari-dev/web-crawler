package interfaces

interface IDataAnalyzer {
    fun getURLStats(url: String): String
    fun getHTMLStats(html: String): String
}