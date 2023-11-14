package storage.interfaces

import core.dto.URLData

interface IFrontierV2 {
    fun update(host: String, urls: List<String>)
//    fun deleteQueue(host: String)
    fun pullFrom(host: String): String?
    fun assign(id: Int, host: String)
    fun unassign(id: Int, host: String)
    fun getAvailableQueue(): String?
}