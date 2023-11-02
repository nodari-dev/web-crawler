package application.interfaces

import core.dto.WebPage

interface IContentProcessor {
    fun processWebPage(webPage: WebPage)
}