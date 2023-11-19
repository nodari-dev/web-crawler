package storage

import core.dto.URLInfo
import infrastructure.repository.interfaces.IFrontierRepository
import mu.KLogger
import storage.interfaces.IFrontier

class Frontier(
    private val frontierRepository: IFrontierRepository,
    private val logger: KLogger,
): IFrontier {

    override fun update(host: String, urls: List<URLInfo>) {
        frontierRepository.update(host, urls)
        logger.info("new data for host: $host")
    }

    override fun pullFrom(host: String): URLInfo? {
        return frontierRepository.get(host)
    }

    override fun assign(id: Int, host: String){
        frontierRepository.assignCrawler(id, host)
        logger.info("assigned crawler $id to $host")
    }

    override fun unassign(id: Int, host: String){
        frontierRepository.unassignCrawler(id, host)
        logger.info("removed crawler $id from $host")
    }

    override fun getAvailableQueue(): String?{
        return frontierRepository.getAvailableQueue()
    }
}