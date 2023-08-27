import analyzer.SEODataAnalyzer
import fetcher.Fetcher

fun main() {
//    CommunicationManager.startCrawling(listOf("https://ecospace.org.ua"))
    val html = Fetcher().getPageHTML("https://magecloud.agency")
    if(html != null){
        SEODataAnalyzer().test(html)
    }
}