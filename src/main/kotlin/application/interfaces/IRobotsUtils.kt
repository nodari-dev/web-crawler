package application.interfaces

import core.dto.URLInfo

interface IRobotsUtils {
    fun getDisallowedURLs(host: String): List<URLInfo>
}