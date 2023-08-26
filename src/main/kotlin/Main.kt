import analyzer.DataAnalyzer
import fetcher.Fetcher

fun main() {
//    CommunicationManager.startCrawling(listOf("https://ecospace.org.ua"))
    val html = Fetcher().getPageHTML("https://ecospace.org.ua/sea")
    if(html != null){
        DataAnalyzer().test(html)
    }
}