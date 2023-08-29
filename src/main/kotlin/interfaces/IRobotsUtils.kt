package interfaces

import dto.HashedUrlPair

interface IRobotsUtils {
    fun getDisallowedURLs(host: String): List<HashedUrlPair>
}