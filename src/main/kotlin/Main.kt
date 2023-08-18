import analyzer.DataAnalyzer
import communication.CommunicationManager
import fetcher.Fetcher

fun main() {
    val communicationManager = CommunicationManager
    communicationManager.addStartingPointURLs(listOf("https://ecospace.org.ua"))
    communicationManager.start()
}