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
            println("added $hostRecord")
            storage.add(hostRecord)
        }
    }
    override fun getHostRecord(host: String): HostRecord?{
        synchronized(mutex){
            return storage.firstOrNull{hostRecord -> hostRecord.url == host}
        }
    }

    override fun isHostDefined(host: String): Boolean{
        synchronized(mutex){
            return storage.any { hostRecord ->  hostRecord.url == host}
        }
    }

    override fun isURLBanned(host: String, url: String): Boolean{
        synchronized(mutex){
            val hostRecord = getHostRecord(host)
            return hostRecord?.bannedURLs?.any { bannedURL -> url.contains(bannedURL.value) } ?: false
        }
    }

    internal fun clear(){
        storage.clear()
    }
 }