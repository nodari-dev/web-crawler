package application.interfaces

import core.dto.URLData

interface IURLParser {
    fun getURLs(document: String): List<URLData>
    fun getHostWithProtocol(document: String): String
}