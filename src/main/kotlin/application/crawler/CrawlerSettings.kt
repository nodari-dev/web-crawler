package application.crawler

import configuration.Configuration.CRAWLING_DELAY
import core.dto.URLInfo

class CrawlerSettings(
    var id: Int = 0,
    var host: String = "",
    var bannedURLs: List<URLInfo> = emptyList(),
    private var delay: Long = CRAWLING_DELAY,
){

    fun setNewDelay(newDelay: Long) {
        delay = if(newDelay.toInt() == -1) CRAWLING_DELAY else newDelay * 100
    }

    fun getDelay(): Long{
        return delay
    }

    fun toDefaults(){
        host = "";
        delay = CRAWLING_DELAY
        bannedURLs = emptyList()
    }
}
