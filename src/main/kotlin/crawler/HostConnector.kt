package crawler
import mu.KotlinLogging

data class HostConnector(val id: Int){
    private val kotlinLogging = KotlinLogging
    private val logger = kotlinLogging.logger("crawler.HostConnector:${id}")

    var host: String? = null

    fun connect(newHost: String){
        logger.info ("Crawler[$id] connected to queue with host: $host")
        host = host
    }

    fun disconnect(){
        logger.info ("Crawler[$id] disconnected from queue")
        host = null
    }

    fun hasConnection(): Boolean{
        return host != null
    }

}