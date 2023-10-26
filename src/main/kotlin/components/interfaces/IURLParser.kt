package components.interfaces

import core.dto.WebLink

interface IURLParser {
    fun getURLs(document: String): List<WebLink>
    fun getHostWithProtocol(document: String): String
}