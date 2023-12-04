package infrastructure.repository.interfaces

import core.dto.RobotsData

interface IRobotsRepository {
    fun update(host: String, robotsData: RobotsData)
    fun get(host: String): RobotsData?
}