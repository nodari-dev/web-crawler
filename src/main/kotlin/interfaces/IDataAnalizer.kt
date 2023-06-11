package interfaces

import dto.Page

interface IDataAnalyzer {
    fun getURLStats(url: String): String
    fun getPageStats(page: Page): String
}