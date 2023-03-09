package services

import Frontier
import PagesDataStore
import dao.CrawlerDao

class Crawler() {
    // 1. fetch data

    private fun crawlPage(){
        // open connection and set timeout
        // get specific data to make analytics
    }

    fun start(){
        Frontier().getURLs().forEach{item -> println("123") }
    }

    fun getURLs(page: String): List<String> {
        val urls = mutableListOf<String>()
        val regex = "<a(?:[^>]*)href=(['\\\"])((http).+?)\\1".toRegex()
        regex.findAll(page).forEach { match -> match.groups[2]?.value?.let { urls.add(it) } }

        return urls
    }
}