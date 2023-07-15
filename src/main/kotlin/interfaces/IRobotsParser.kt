package interfaces

import dto.FormattedURL

interface IRobotsParser {
    fun getRobotsDisallowed(document: String): List<FormattedURL>
}