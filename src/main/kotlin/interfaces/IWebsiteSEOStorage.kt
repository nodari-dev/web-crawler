package interfaces

import dto.SEORecord

interface IWebsiteSEOStorage {
    fun updateOrCreateRecord(host: String, content: String)
    fun getSEORecord(host: String): SEORecord?
}