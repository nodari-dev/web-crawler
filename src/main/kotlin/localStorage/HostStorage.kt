package localStorage

import dto.FormattedURL
import dto.HostRecord
import interfaces.IHostsStorage

object HostsStorage: IHostsStorage {
    private val mutex = Object()
    private val storage: MutableList<HostRecord> = mutableListOf()

    override fun addHostRecord(host: String, bannedURLs: List<FormattedURL>){
        synchronized(mutex){
            val hostRecord = HostRecord(host, bannedURLs)
            storage.add(hostRecord)
        }
    }

    override fun isHostDefined(host: String): Boolean{
        synchronized(mutex){
            return storage.any { hostRecord ->  hostRecord.url == host}
        }
    }

    override fun isURLAllowed(host: String, url: String): Boolean{
        return synchronized(mutex) {
            val hostRecord = getHostRecord(host) ?: return true
            hostRecord.bannedURLs.none { bannedURL -> url.contains(bannedURL.value) }
        }
    }

    private fun getHostRecord(host: String): HostRecord?{
        return storage.firstOrNull{hostRecord -> hostRecord.url == host}
    }

    internal fun clear(){
        storage.clear()
    }
 }