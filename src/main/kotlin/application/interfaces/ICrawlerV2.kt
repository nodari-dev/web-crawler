package application.interfaces

import application.crawler.entities.CrawlerConfig
import application.crawler.entities.CrawlerStatus

interface ICrawlerV2 {
    fun getStatus(): CrawlerStatus
    fun getConfig(): CrawlerConfig
    fun reassign(newHost: String)
    fun terminate()
}