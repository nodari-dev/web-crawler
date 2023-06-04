import crawler.terminalCrawler.TerminalCrawler
import dto.Page

fun main() {
    val test = TerminalCrawler(Page("https://ecospace.org.ua"))
    test.start()
}
