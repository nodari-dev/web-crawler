package application.crawler

import core.dto.URLInfo
import configuration.Configuration.CRAWLING_DELAY

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
