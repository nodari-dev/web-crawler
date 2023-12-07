package infrastructure.repository.interfaces

import core.dto.URLInfo

interface IVisitedURLsRepository {
    fun getOnlyNewURLs(urlInfoList: List<URLInfo>): List<URLInfo>
    fun update(urlInfo: URLInfo)
    fun clear()
    fun isNew(url: String): Boolean
}