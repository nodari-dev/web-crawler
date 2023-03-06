import java.net.URL

class Crawler(url: String) {
    // 1. fetch data
    // 2. Check the identity of page by PageDataStore
    // 3. first time -> create signature



    private val page = URL(url).readText()

    init{
        getUrls(page)
    }

    private fun crawlPage(){

    }

    private fun getUrls(page: String): List<String> {
        val urls = mutableListOf<String>()
        val regex = "<a(?:[^>]*)href=(['\\\"])([(http)(/)].+?)\\1".toRegex()
        regex.findAll(page).forEach { match -> match.groups[2]?.value?.let { urls.add(it) } }

        urls.forEach{item -> println(item)}
        return urls
    }
}