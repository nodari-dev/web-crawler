package interfaces

import dto.CrawlerTypes

interface IController {
    val crawlerType: CrawlerTypes
    fun start()
}