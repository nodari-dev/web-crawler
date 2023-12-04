package application.interfaces

import core.dto.RobotsData

interface IRobotsParser {
    fun getRobotsData(document: String): RobotsData
}