package storage.interfaces

import core.dto.WebLink

interface IFrontier {
    fun pullURL(host: String): WebLink
    fun updateOrCreateQueue(host: String, url: String)
    fun isQueueEmpty(host: String): Boolean
    fun deleteQueue(host: String)
}