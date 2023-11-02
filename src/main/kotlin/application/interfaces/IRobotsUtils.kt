package application.interfaces

import core.dto.WebLink

interface IRobotsUtils {
    fun getDisallowedURLs(host: String): List<WebLink>
}