package storage.interfaces

import core.dto.URLInfo

interface IFrontierV2 {
    fun update(host: String, urls: List<URLInfo>)
//    fun deleteQueue(host: String)
    fun pullFrom(host: String): URLInfo?
    fun assign(id: Int, host: String)
    fun unassign(id: Int, host: String)
    fun getAvailableQueue(): String?
}