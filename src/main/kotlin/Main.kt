import services.Crawler

fun main() {
    val seedUrls: Array<String> = arrayOf(
        "https://cs.wikipedia.org/wiki/Hlavn%C3%AD_strana",
        "https://ecospace.org.ua/mountains",
        "https://ecospace.org.ua/mountains"
    )
    if(seedUrls.isNotEmpty()){
        Frontier().putURLs(seedUrls)
        Crawler(seedUrls)
    } else{
        println("No seed urls provided")
    }
}