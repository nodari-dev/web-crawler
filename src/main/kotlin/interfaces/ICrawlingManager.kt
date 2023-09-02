package interfaces

interface ICrawlingManager {
    fun startCrawling(seeds: List<String>)
    fun extractSEOData(html: String, url: String)
}