package application.interfaces

import mu.KLogger

interface IFetcher {
    val logger: KLogger
    fun getPageHTML(url: String): String?
}