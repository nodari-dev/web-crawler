package interfaces

import dto.FormattedURL
import dto.FrontierQueue
import dto.URLRecord

interface IFrontier {
    fun pullURLRecord(host: String): URLRecord?
    fun updateOrCreateQueue(host: String, formattedURL: FormattedURL)
    fun getQueue(host: String): FrontierQueue?
    fun pickFreeQueue(): String?
}