package storage

import core.dto.URLInfo
import infrastructure.repository.interfaces.IHostsRepository
import mu.KLogger
import storage.interfaces.IHostsStorage

class HostsStorage(
    private val storageRepository: IHostsRepository,
    private val logger: KLogger,
): IHostsStorage {
    override fun updateHost(host: String, urlsInfo: List<URLInfo>) {
        storageRepository.update(host, urlsInfo)
        logger.info("Updated data for host: $host")
    }

    override fun isURLAllowed(host: String, urlInfo: URLInfo): Boolean {
        val disallowedURLs = storageRepository.get(host)

        return if(disallowedURLs.isEmpty()){
            true
        } else{
            disallowedURLs.any{disallowedURL -> !urlInfo.link.contains(disallowedURL)}
        }
    }

    override fun doesHostExist(host: String): Boolean {
        val disallowedURLs = storageRepository.get(host)
        return disallowedURLs.isEmpty()
    }
}