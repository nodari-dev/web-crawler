package crawler

import interfaces.IDataAnalyzer

class DataAnalyzer: IDataAnalyzer {
    // TODO: Analyze url and fetched data

    override fun getURLStats(url: String): String{
        return ""
    }

    override fun getHTMLStats(html: String): String{
        return ""
    }
}