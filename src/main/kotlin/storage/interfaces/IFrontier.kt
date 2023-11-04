package storage.interfaces

import core.dto.URLData

interface IFrontier {
    fun pullURL(host: String): URLData
    fun updateOrCreateQueue(host: String, url: String)
    fun isQueueEmpty(host: String): Boolean
    fun deleteQueue(host: String)
}