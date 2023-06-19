package interfaces

import crawler.TerminalCrawler

interface ICrawlerUtils {
    val crawler: TerminalCrawler
    fun canProcessURL(url: String): Boolean
    fun canProceedCrawling(): Boolean
    fun killCrawler()
}