package storage

import core.dto.RobotsData
import infrastructure.repository.interfaces.IRobotsRepository
import mu.KLogger
import storage.interfaces.IRobotsStorage

class RobotsStorage(
    private val robotsRepository: IRobotsRepository,
    private val logger: KLogger,
): IRobotsStorage {
    override fun update(host: String, robotsData: RobotsData) {
        robotsRepository.update(host, robotsData)
        logger.info("Updated data for host: $host")
    }

    override fun get(host: String): RobotsData? {
        return robotsRepository.get(host)
    }
}