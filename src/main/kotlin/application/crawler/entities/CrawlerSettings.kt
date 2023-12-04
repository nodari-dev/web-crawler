package application.crawler.entities

import configuration.Configuration.CRAWLING_DELAY
import core.dto.URLInfo

class CrawlerSettings(
    var id: Int = 0,
    var host: String = "",
    var delay: Long = CRAWLING_DELAY,
    var bannedURLs: List<URLInfo> = emptyList()
){

    fun setNewDelay(newDelay: Long) {
        delay = if(newDelay.toInt() == -1) CRAWLING_DELAY else newDelay * 100
    }

    fun delay(): Long{
        return delay
    }

    fun toDefaults(){
        host = "";
        delay = CRAWLING_DELAY
        bannedURLs = emptyList()
    }
}
