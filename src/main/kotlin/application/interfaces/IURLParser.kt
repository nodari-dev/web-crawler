package application.interfaces

import core.dto.URLInfo

interface IURLParser {
    fun getURLs(document: String): List<URLInfo>
    fun getHostname(document: String): String
}