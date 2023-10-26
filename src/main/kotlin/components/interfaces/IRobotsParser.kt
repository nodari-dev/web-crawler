package components.interfaces

import core.dto.WebLink

interface IRobotsParser {
    fun getRobotsDisallowed(document: String): List<WebLink>
}