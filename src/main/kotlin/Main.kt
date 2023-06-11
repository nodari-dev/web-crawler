import crawler.SingleTerminalCrawler
import dto.Page

fun main() {
    val test = SingleTerminalCrawler(Page("https://ecospace.org.ua"))
    test.start()
}
