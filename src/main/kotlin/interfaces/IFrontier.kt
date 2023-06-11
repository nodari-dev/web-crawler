package interfaces

import dto.CrawlerModes

interface IFrontier {
    fun addURL(value: String)
    fun getURL(): String?
}