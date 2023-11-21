package application.crawler.entities

data class CrawlerSettings(
    var id: Int = 0,
    var host: String = "",
    var recoveryMode: Boolean = false
)
