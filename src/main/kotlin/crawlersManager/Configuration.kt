package crawlersManager

import dto.CrawlerModes

object Configuration{
    const val NUMBER_OF_CRAWLERS: Int = 10
    const val TIME_BETWEEN_FETCHING: Long = 3500
    const val CRAWLING_LIMIT: Int = 20
    val MODE: CrawlerModes = CrawlerModes.TERMINAL_CRAWLER
}