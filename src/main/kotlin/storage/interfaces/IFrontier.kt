package storage.interfaces

import application.interfaces.ISubscriber
import core.dto.URLInfo

interface IFrontier {
    fun update(host: String, urls: List<URLInfo>)
    fun pullFrom(host: String): URLInfo?
    fun assign(id: Int, host: String)
    fun unassign(id: Int, host: String)
    fun getAvailableQueue(): String?
    fun subscribe(subscriber: ISubscriber)
}