package interfaces

import dto.FormattedURL

interface IFrontier {
    fun pullURL(host: String): FormattedURL?
    fun updateOrCreateQueue(host: String, formattedURL: FormattedURL)
}