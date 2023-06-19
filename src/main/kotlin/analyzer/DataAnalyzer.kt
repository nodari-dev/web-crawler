package analyzer

import interfaces.IDataAnalyzer

class DataAnalyzer: IDataAnalyzer {
    // TODO: Analyze url and fetched data

    override fun getURLStats(url: String): String{
        return ""
    }

    override fun getPageStats(): String{
        println()
        return ""
    }
}