package interfaces

import dto.FormattedURL

interface IURLParser {
    fun getURLs(document: String): List<FormattedURL>
    fun getHostWithProtocol(document: String): String
}