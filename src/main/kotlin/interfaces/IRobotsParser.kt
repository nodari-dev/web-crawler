package interfaces

import dto.HashedUrlPair

interface IRobotsParser {
    fun getRobotsDisallowed(document: String): List<HashedUrlPair>
}