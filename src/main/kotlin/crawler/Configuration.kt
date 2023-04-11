package crawler

data class Configuration(
    val numberOfCrawlers: Int,
    val frontQueueTable: String,
    val backQueueTable: String,
    val visitedUrlsTable: String
)