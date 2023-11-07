package storage.interfaces

import core.dto.URLData

interface IFrontierV2 {
    fun pullWebLink(host: String): URLData?
    fun updateOrCreateQueue(host: String, urls: List<String>)
    fun deleteQueue(host: String)
    fun assignCrawler(host: String, crawlerId: String)
    fun unassignCrawler(host: String, crawlerId: String)
}