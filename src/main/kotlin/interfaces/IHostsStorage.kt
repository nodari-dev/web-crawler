package interfaces

import dto.HashedUrlPair

interface IHostsStorage {
    fun provideHost(host: String, bannedURLs: List<HashedUrlPair>)
    fun isURLAllowed(host: String, url: String): Boolean
}