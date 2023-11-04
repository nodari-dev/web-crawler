package application.interfaces

import mu.KLogger

interface IFetcher {
    fun getPageHTML(url: String): String?
}