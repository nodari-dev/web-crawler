package localStorage

import analyzer.DataAnalyzer
import dto.FormattedURL
import dto.SEORecord
import interfaces.ISEOStorage
import java.util.concurrent.locks.ReentrantLock

object SEOStorage: ISEOStorage{
    private val storage = mutableListOf<SEORecord>()
    private val dataAnalyzer = DataAnalyzer()
    private val mutex = ReentrantLock()

    internal fun clear(){
        storage.clear()
    }

    override fun updateOrCreateSEORecord(host: String, url: String, html: String) {
        mutex.lock()
        try{
            when(isSEORecordDefined(host)){
                true -> updateSEORecord(host, url ,html)
                else -> createSEORecord(host, url, html)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun isSEORecordDefined(host: String): Boolean{
        return storage.any { seoRecord -> seoRecord.host == host }
    }

    private fun updateSEORecord(host: String, url: String, html: String){

    }

    private fun createSEORecord(host: String, url: String, html: String){

    }

    private fun getSEORecord(host: String): SEORecord? {
        return storage.firstOrNull { it.host == host }
    }
}