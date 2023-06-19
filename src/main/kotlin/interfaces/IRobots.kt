package interfaces

interface IRobots {
    fun getDisallowedURLs(url: String): List<String>
}