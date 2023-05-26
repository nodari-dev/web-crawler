package crawler

object Configuration{
    const val numberOfCrawlers: Int = 2
    const val timeBetweenFetching: Int = 10000
    const val frontQueueTable: String = "frontq"
    const val backQueueTable: String = "backq"
    const val visitedUrlsTable: String = "visited"
}