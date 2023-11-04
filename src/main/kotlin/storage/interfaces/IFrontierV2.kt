package storage.interfaces

import core.dto.URLData

interface IFrontierV2 {
    fun pullWebLink(host: String): URLData?
    fun updateOrCreateQueue(host: String, url: String)
    fun isQueueEmpty(host: String): Boolean
    fun deleteQueue(host: String)
}