package core.interfaces.components

import core.dto.HashedURLPair

interface IRobotsUtils {
    fun getDisallowedURLs(host: String): List<HashedURLPair>
}