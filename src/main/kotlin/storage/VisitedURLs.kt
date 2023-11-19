package storage

import core.dto.URLInfo
import infrastructure.repository.interfaces.IVisitedURLsRepository
import storage.interfaces.IVisitedURLs

class VisitedURLs(
    private val visitedURLsRepository: IVisitedURLsRepository
): IVisitedURLs {
    override fun update(urlInfo: URLInfo) {
        visitedURLsRepository.update(urlInfo)
    }

    override fun filterByNewOnly(urlInfoList: List<URLInfo>): List<URLInfo> {
        return visitedURLsRepository.getOnlyNewURLs(urlInfoList).toSet().toList()
    }

    override fun isValid(url: String): Boolean {
        return visitedURLsRepository.isNew(url)
    }
}