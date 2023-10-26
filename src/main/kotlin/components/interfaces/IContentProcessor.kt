package components.interfaces

import core.dto.WebPage

interface IContentProcessor {
    fun processWebPage(webPage: WebPage)
}