package hostsStorage

import dto.Host

object HostsStorage {
    private val mutex = Object()
    private val storage: MutableList<Host> = mutableListOf()

    fun add(hostURL: String, bannedURLs: List<String>){
        synchronized(mutex){
            storage.add(Host(hostURL, bannedURLs))
        }
    }

    fun get(url: String): Host?{
        var result: Host? = null

        for (host in storage){
            if(host.url === url){
                result = host
            }
        }
        return result
    }
 }