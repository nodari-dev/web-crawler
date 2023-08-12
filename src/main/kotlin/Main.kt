import communicationManager.CommunicationManager
import dto.FormattedURL
import frontier.FrontierRedis

fun main() {
    val communicationManager = CommunicationManager
    communicationManager.addStartingPointURLs(listOf("https://ecospace.org.ua"))
    communicationManager.start()
}
