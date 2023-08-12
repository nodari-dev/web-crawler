package interfaces

import dto.FormattedURL
import dto.FrontierRecord

interface IFrontierRedis {
    fun pullURLRecord(host: String): FrontierRecord?
    fun updateOrCreateQueue(host: String, formattedURL: FormattedURL)
}