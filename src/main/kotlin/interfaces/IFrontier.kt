package interfaces

import dto.HashedUrlPair

interface IFrontier {
    fun pullURL(host: String): HashedUrlPair?
    fun updateOrCreateQueue(host: String, hashedUrlPair: HashedUrlPair)
}