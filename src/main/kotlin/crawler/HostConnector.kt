import mu.KotlinLogging

class HostConnector(id: Int){
    private val kotlinLogging = KotlinLogging
    private val logger = kotlinLogging.logger("HostConnector:${id}")
    private val crawlerId = id

    private var selectedHost: String? = null

    fun connect(host: String){
        logger.info ("Crawler[$crawlerId] connected to queue with host: $host")
        selectedHost = host
    }

    fun disconnect(){
        logger.info ("Crawler[$crawlerId] disconnected from queue")
        selectedHost = null
    }

    fun getHost(): String?{
        return selectedHost
    }

    fun hasConnection(): Boolean{
        return selectedHost != null
    }

}