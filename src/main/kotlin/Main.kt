import crawler.CrawlersController
import fetcher.Fetcher
import parser.Parser
import parser.RegexPatterns
import parser.RegexPatterns.UNSUPPORTED_FILETYPES

fun main() {
//    val test = SingleHostCrawl(Page("http://example.com/"))
//    test.start()

    // http://example.com/
//    val controller = CrawlersController()
//    controller.start()

//    val fetcher = Fetcher()
//    println(fetcher.getPageContent("http://example.com/"))
    val urls = Parser.getFilteredUrls(
            "<a href='http://example.com'>123</a>" +
            "<a href='http://example.css'>123</a>" +
            "<a href='http://example.gif'>123</a>")

    val result = UNSUPPORTED_FILETYPES.containsMatchIn("http://example.gif")
    println(result)

}