package interfaces

import dto.HashedURLPair

interface IRobotsParser {
    fun getRobotsDisallowed(document: String): List<HashedURLPair>
}