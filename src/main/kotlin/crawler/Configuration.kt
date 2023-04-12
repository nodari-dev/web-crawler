package crawler

data class Configuration(
    val numberOfCrawlers: Int,
    val secondsBetweenFetching: Int,
    val frontQueueTable: String,
    val backQueueTable: String,
    val visitedUrlsTable: String
)