import crawlersManager.CrawlersManager
import dto.CrawlerModes
import dto.Page
import parser.Parser
import java.net.URL

fun main() {
//    val manager = CrawlersManager(CrawlerModes.TERMINAL_CRAWLER)
//    manager.addSeed("https://ecospace.org.ua")
//    manager.startCrawling()

    val html = ""

    val result = Parser.getImagesAlt(html)
    println(result)
}
