import communication.CommunicationManager

fun main() {
    val communicationManager = CommunicationManager
    communicationManager.addStartingPointURLs(listOf("https://ecospace.org.ua"))
    communicationManager.startCrawling()
}