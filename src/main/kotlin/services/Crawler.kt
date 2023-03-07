package services

import PagesDataStore
import dao.CrawlerDao

class Crawler(seedUrls: Array<String>) {
    // 1. fetch data
    // 2. Check the identity of page by PageDataStore
    // 3. first time -> create signature
    private val pagesStore = PagesDataStore()

    init{
        // check url before fetching

        seedUrls.forEach { url ->
            if(!pagesStore.wasCrawled(url)){
                pagesStore.putUrl(url)
//                getUrls(URL(url).readText())
            }
         }
        pagesStore.checkStore()
    }

    private fun crawlPage(){
        // open connection and set timeout
        // get specific data to make analytics
    }

    fun parseURLs(page: String): List<String> {
        val urls = mutableListOf<String>()
        val regex = "<a(?:[^>]*)href=(['\\\"])((http).+?)\\1".toRegex()
        regex.findAll(page).forEach { match -> match.groups[2]?.value?.let { urls.add(it) } }

        return urls
    }
}