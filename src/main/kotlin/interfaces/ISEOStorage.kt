package interfaces

interface ISEOStorage {
    fun updateOrCreateSEOEntry(host: String, url: String, html: String)
}