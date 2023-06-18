package analyzer

import dto.Page
import interfaces.IDataAnalyzer

class DataAnalyzer: IDataAnalyzer {
    // TODO: Analyze url and fetched data

    override fun getURLStats(url: String): String{
        return ""
    }

    override fun getPageStats(page: Page): String{
        println()
        return ""
    }
}