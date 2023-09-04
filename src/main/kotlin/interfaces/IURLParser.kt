package interfaces

import dto.HashedURLPair

interface IURLParser {
    fun getURLs(document: String): List<HashedURLPair>
    fun getHostWithProtocol(document: String): String
}