package interfaces

import crawler.TerminalCrawler

interface ICrawlerUtils {
    fun canProcessURL(url: String, host: String?): Boolean
    fun canProceedCrawling(): Boolean
}