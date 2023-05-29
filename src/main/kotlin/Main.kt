
import fetcher.Fetcher
import parser.Parser

fun main() {
//    val test = SingleHostCrawl(Page("http://example.com/"))
//    test.start()

//    http://example.com/
//    val controller = CrawlersController()
//    controller.start()

    val fetcher = Fetcher
    println(fetcher.getPageContent("http://test.xyz/"))
}
