package crawler

data class Configuration(
    val numberOfCrawlers: Int,
    val timeBetweenFetching: Long,
    val frontQueueTable: String,
    val backQueueTable: String,
    val visitedUrlsTable: String,
)