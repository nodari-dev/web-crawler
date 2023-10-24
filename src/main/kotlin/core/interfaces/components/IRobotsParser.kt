package core.interfaces.components

import core.dto.WebLink

interface IRobotsParser {
    fun getRobotsDisallowed(document: String): List<WebLink>
}