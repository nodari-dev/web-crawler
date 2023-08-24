package interfaces

import dto.HashedUrlPair

interface IURLParser {
    fun getURLs(document: String): List<HashedUrlPair>
    fun getHostWithProtocol(document: String): String
}