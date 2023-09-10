package interfaces

import dto.HashedURLPair

interface IRobotsUtils {
    fun getDisallowedURLs(host: String): List<HashedURLPair>
}