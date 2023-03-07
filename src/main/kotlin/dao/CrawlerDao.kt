package dao

interface CrawlerDao {
    fun crawlPage(url: String): Unit
//    private fun parseURLs(page: String): List<String>
}