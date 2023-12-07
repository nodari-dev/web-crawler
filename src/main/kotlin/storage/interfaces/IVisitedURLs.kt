package storage.interfaces

import core.dto.URLInfo

interface IVisitedURLs {
    fun update(urlInfo: URLInfo)
    fun filterByNewOnly(urlInfoList: List<URLInfo>): List<URLInfo>
    fun isValid(url: String): Boolean
}