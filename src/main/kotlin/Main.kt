
fun main() {
    val seedUrls: Array<String> = arrayOf("https://cs.wikipedia.org/wiki/Hlavn%C3%AD_strana")
    if(seedUrls.isNotEmpty()){
        println("CRAWLING...")
        Crawler(seedUrls)
    } else{
        println("No seed urls provided")
    }
}