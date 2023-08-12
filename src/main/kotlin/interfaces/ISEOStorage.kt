package interfaces

interface ISEOStorage {
    fun updateOrCreateSEORecord(host: String, url: String, html: String)
}