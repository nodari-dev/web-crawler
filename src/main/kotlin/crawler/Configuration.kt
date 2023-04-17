package crawler

object Configuration{
    const val numberOfCrawlers: Int = 2
    const val timeBetweenFetching: Long = 1000
    const val frontQueueTable: String = "frontq"
    const val backQueueTable: String = "backq"
    const val visitedUrlsTable: String = "visited"
}