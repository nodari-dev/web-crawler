package interfaces

import dto.HashedURLPair

interface IFrontier {
    fun pullURL(host: String): HashedURLPair?
    fun updateOrCreateQueue(host: String, url: String)
    fun isQueueEmpty(host: String): Boolean
    fun deleteQueue(host: String)
}