package storage.interfaces

import core.dto.RobotsData

interface IRobotsStorage {
    fun update(host: String, robotsData: RobotsData)
    fun get(host: String): RobotsData?
}