package core.interfaces.components

import core.dto.WebPage

interface IContentProcessor {
    fun processWebPage(webPage: WebPage)
}