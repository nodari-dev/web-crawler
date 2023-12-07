package storage

import mu.KLogger

import core.dto.RobotsData
import storage.interfaces.IRobotsStorage
import infrastructure.repository.interfaces.IRobotsRepository

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