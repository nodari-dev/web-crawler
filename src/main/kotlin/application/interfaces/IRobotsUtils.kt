package application.interfaces

import core.dto.URLData

interface IRobotsUtils {
    fun getDisallowedURLs(host: String): List<URLData>
}