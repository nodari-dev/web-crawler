package infrastructure.repository.interfaces

import core.dto.URLInfo

interface IFrontierRepository {
    fun clear()
    fun get(host: String): URLInfo?
    fun update(host: String, list: List<URLInfo>)
    fun assignCrawler(id: Int, host: String)
    fun unassignCrawler(id: Int, host: String)
    fun unassignAllCrawlers()
    fun getAvailableQueue(): String?
}