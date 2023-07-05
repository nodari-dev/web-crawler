package interfaces

interface IRobotsParser {
    fun getRobotsDisallowed(document: String): List<String>
}