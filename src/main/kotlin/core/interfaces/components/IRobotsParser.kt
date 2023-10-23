package core.interfaces.components

import core.dto.HashedURLPair

interface IRobotsParser {
    fun getRobotsDisallowed(document: String): List<HashedURLPair>
}