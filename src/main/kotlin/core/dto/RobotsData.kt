package core.dto

data class RobotsData(
    val bannedURLs: List<URLInfo>,
    val crawlDelay: Long = -1
)
