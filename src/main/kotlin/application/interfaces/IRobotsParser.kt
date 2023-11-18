package application.interfaces

import core.dto.URLInfo

interface IRobotsParser {
    fun getRobotsDisallowed(document: String): List<URLInfo>
}