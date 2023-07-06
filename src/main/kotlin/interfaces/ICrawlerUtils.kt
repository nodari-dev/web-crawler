package interfaces

import crawler.TerminalCrawler

interface ICrawlerUtils {
    fun isURLValid(url: String, host: String): Boolean
    fun canProceedCrawling(): Boolean
}