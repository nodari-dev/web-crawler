package interfaces

import dto.FrontierQueue
import dto.URLRecord

interface IFrontier {
    fun pullURLRecord(host: String): URLRecord?
    fun updateOrCreateQueue(host: String, urlRecord: URLRecord)
    fun getQueue(host: String): FrontierQueue?
    fun pickFreeQueue(): String?
}