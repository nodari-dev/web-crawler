package storage.hosts

import infrastructure.repository.interfaces.IHostsRepository
import storage.interfaces.IHostsStorage

class HostsStorage(
    private val storageRepository: IHostsRepository
): IHostsStorage {
    override fun updateHost(host: String, urls: List<String>) {
        storageRepository.update(host, urls)
    }

    override fun isURLAllowed(host: String, url: String): Boolean {
        val disallowedURLs = storageRepository.get(host)

        return if(disallowedURLs.isEmpty()){
            true
        } else{
            disallowedURLs.any{disallowedURL -> !url.contains(disallowedURL)}
        }
    }

    override fun doesHostExist(host: String): Boolean {
        val disallowedURLs = storageRepository.get(host)
        return disallowedURLs.isEmpty()
    }
}