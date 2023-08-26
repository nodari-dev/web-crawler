package interfaces

interface IFetcher {
    fun getPageHTML(url: String): String?
}