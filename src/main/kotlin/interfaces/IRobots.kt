package interfaces

import dto.FormattedURL

interface IRobots {
    fun getDisallowedURLs(host: String, url: String): List<FormattedURL>
}