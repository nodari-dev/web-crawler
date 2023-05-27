package interfaces

interface IFetcher {
    fun getPageContent(url: String): String?
}