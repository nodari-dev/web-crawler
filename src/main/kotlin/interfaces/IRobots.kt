package interfaces

import dto.FormattedURL

interface IRobots {
    fun getDisallowedURLs(host: String): List<FormattedURL>
}