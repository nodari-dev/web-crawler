package interfaces

import dto.HostWithProtocol

interface IURLParser {
    fun getURLs(document: String): List<String>
    fun getHostWithProtocol(document: String): HostWithProtocol
}