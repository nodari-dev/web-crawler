package application.interfaces

import application.crawler.entities.CrawlerStatus

interface ICrawlerV2 {
    fun getStatus(): CrawlerStatus
    fun terminate()
}