import services.Crawler

fun main() {
    val seedUrls: List<String> = listOf(
        "https://cs.wikipedia.org/wiki/Hlavn%C3%AD_strana",
    ).distinct()

    if(seedUrls.isNotEmpty()){
        val crawler: Crawler = Crawler(seedUrls)
        crawler.start()
    } else{
        println("No seed urls provided")
    }
}