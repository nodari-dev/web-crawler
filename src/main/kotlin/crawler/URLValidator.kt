package crawler

import dto.HashedURLPair
import interfaces.IURLValidator
import storage.hosts.HostsStorage
import storage.url.URLStorage

class URLValidator: IURLValidator {
    var hostsStorage = HostsStorage
    var urlStorage = URLStorage

    override fun canProcessURL(host: String, hashedUrlPair: HashedURLPair?): Boolean{
        if(hashedUrlPair == null){
            return false
        }

        val isNew = urlStorage.doesNotExist(hashedUrlPair.getHash())
        val isAllowed = hostsStorage.isURLAllowed(host, hashedUrlPair.url)
        return isNew && isAllowed
    }
}