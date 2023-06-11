package crawlerManager

object Configuration{
    const val NUMBER_OF_CRAWLERS: Int = 2
    const val TIME_BETWEEN_FETCHING: Long = 3500
    const val CONNECTION_TIMEOUT: Int = 150000
    const val FRONT_QUEUE_TABLE: String = "frontq"
    const val BACK_QUEUE_TABLE: String = "backq"
    const val VISITED_URLS_TABLE: String = "visited"
}