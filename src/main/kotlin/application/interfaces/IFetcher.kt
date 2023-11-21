package application.interfaces

interface IFetcher {
    fun downloadSanitizedHTML(url: String): String?
}