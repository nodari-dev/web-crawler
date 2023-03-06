import java.net.URL

class Crawler(seedUrls: Array<String>) {
    // 1. fetch data
    // 2. Check the identity of page by PageDataStore
    // 3. first time -> create signature
    private val seedUrls = seedUrls

    init{
        println(seedUrls.forEach { item -> println(item) })
        // check url before fetching
        seedUrls.forEach { url -> getUrls(URL(url).readText()) }
    }

    private fun crawlPage(){


    }

    private fun getUrls(page: String): List<String> {
        println("getting...")
        val urls = mutableListOf<String>()
        val regex = "<a(?:[^>]*)href=(['\\\"])([(http)].+?)\\1".toRegex()
        regex.findAll(page).forEach { match -> match.groups[2]?.value?.let { urls.add(it) } }

        urls.forEach{item -> println(item)}
        return urls
    }
}