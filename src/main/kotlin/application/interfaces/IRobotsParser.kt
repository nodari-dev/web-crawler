package application.interfaces

import core.dto.URLData

interface IRobotsParser {
    fun getRobotsDisallowed(document: String): List<URLData>
}