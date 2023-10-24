package core.interfaces.components

import core.dto.WebLink

interface IURLParser {
    fun getURLs(document: String): List<WebLink>
    fun getHostWithProtocol(document: String): String
}