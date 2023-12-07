package application.interfaces

interface IFetcher {
    fun downloadHTML(url: String): String?
}