import dto.PageInfo
import services.Crawler

fun main() {
    val seedUrls: List<String> = listOf(
        "https://cs.wikipedia.org/wiki/Hlavn%C3%AD_strana",
        "https://ecospace.org.ua/mountains",
        "https://ecospace.org.ua/mountains"
    ).distinct()

    val nodes: MutableList<PageInfo> = mutableListOf<PageInfo>()

    seedUrls.forEach { url -> nodes.add(PageInfo(url)) }

    if(seedUrls.isNotEmpty()){
        val crawler: Crawler = Crawler(seedUrls)
        crawler.start()
    } else{
        println("No seed urls provided")
    }
}