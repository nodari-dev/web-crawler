package interfaces

import dto.FormattedURL

interface IHostsStorage {
    fun provideHost(host: String, bannedURLs: List<FormattedURL>)
    fun isURLAllowed(host: String, url: String): Boolean
}