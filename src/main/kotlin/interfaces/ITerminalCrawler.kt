package interfaces

import dto.Page

interface ITerminalCrawler {
    val startPage: Page
    val maxURLsToFetch: Int
    val fetchOnlyProvidedHost: Boolean
    fun start()
}