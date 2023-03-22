package services

import dao.Regex.Values.A_TAG
import dao.Regex.Values.GROUP_INDEX
import dto.PageInfo
import java.net.URL

class Crawler(urls: List<String>) {
    private val seedUrls = urls
    // 1. fetch data

    private val frontier = Frontier()

    private fun crawlPage(url: String){
        // open connection and set timeout
        // get specific data to make analytics
    }

    fun start(){
        seedUrls.forEach{url -> frontier.putURL(PageInfo(url)) }
        test()
    }

    private fun test(){
        frontier.getURLs().forEach{url -> println() }
//        val page = URL(websiteLink).readText()
    }

    private fun getChildURLs(page: String): MutableList<PageInfo> {
        val childUrls: MutableList<PageInfo> = mutableListOf<PageInfo>()

        A_TAG.findAll(page).mapNotNull { match ->
            val singlePageInfo = PageInfo(match.groups[GROUP_INDEX]?.value)

            childUrls.add(singlePageInfo)
        }

        return childUrls
    }
}