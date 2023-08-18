package interfaces

import dto.FormattedURL
import dto.FrontierRecord

interface IFrontierRedis {
    fun pullURL(host: String): FrontierRecord?
    fun updateOrCreateQueue(host: String, formattedURL: FormattedURL)
}