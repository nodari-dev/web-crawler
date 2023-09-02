package interfaces

import dto.HashedUrlPair

interface IHostsStorage {
    fun provideHost(host: String)
    fun deleteHost(host: String)
    fun isURLAllowed(host: String, url: String): Boolean
}