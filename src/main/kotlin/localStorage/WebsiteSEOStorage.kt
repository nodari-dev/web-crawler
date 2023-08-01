package localStorage

import analyzer.DataAnalyzer
import dto.SEOContent
import dto.SEORecord
import interfaces.IWebsiteSEOStorage

object WebsiteSEOStorage: IWebsiteSEOStorage{
    private val mutex = Object()
    private val storage = mutableListOf<SEORecord>()
    private val dataAnalyzer = DataAnalyzer()

    internal fun clear(){
        storage.clear()
    }

    override fun updateOrCreateRecord(host: String, content: String) {
        TODO("Not yet implemented")
    }

    override fun getSEORecord(host: String): SEORecord? {
        TODO("Not yet implemented")
    }
}