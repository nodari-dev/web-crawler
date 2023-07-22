package localStorage

import dto.SEORecord
import interfaces.IWebsiteSEOStorage

object WebsiteSEOStorage: IWebsiteSEOStorage{
    private val mutex = Object()
    private val storage = mutableListOf<SEORecord>()

    internal fun clear(){
        storage.clear()
    }

    override fun updateOrCreateRecord(host: String, content: String) {
        synchronized(mutex){
            if(isRecordDefined(host)){
                updateRecord(host, content)
            } else{
                val record = SEORecord(host, content)
                createRecord(record)
            }
        }
    }

    private fun updateRecord(host: String, newContent: String){
        synchronized(mutex){
            storage.forEach { record ->
                if(record.host == host){
                    record.content += newContent
                }
            }
        }
    }

    private fun createRecord(record: SEORecord){
        synchronized(mutex){
            storage.add(record)
        }
    }

    private fun isRecordDefined(host: String): Boolean{
        return synchronized(mutex){
            storage.any { record -> record.host == host }
        }
    }

    override fun getSEORecord(host: String): SEORecord? {
        return synchronized(mutex){
            storage.firstOrNull { it.host == host }
        }
    }
}