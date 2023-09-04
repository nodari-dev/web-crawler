package interfaces

import dto.HashedURLPair

interface IFrontier {
    fun pullURL(host: String): HashedURLPair?
    fun updateOrCreateQueue(host: String, hashedUrlPair: HashedURLPair)
    fun isQueueEmpty(host: String): Boolean
    fun deleteQueue(host: String)
}