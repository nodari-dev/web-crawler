package frontier

import dto.FormattedURL
import dto.FrontierQueue
import dto.URLRecord
import interfaces.IFrontier
import mu.KotlinLogging

object Frontier: IFrontier {
    private val queues = mutableListOf<FrontierQueue>()
    private val mutex = Object()
    private val logger = KotlinLogging.logger("Frontier")

    override fun updateOrCreateQueue(host: String, formattedURL: FormattedURL) {
        val urlRecord = URLRecord(formattedURL)
        when(isQueueDefined(host)){
            true -> updateQueue(host, urlRecord)
            else -> createQueue(host, urlRecord)
        }
    }

    private fun isQueueDefined(host: String): Boolean{
        return queues.any { queue -> queue.host == host }
    }

    private fun updateQueue(host: String, urlRecord: URLRecord) {
        synchronized(mutex){
            val queue = getQueue(host)
            queue?.urlRecords?.add(urlRecord)
            mutex.notifyAll()
        }
    }

    private fun createQueue(host: String, urlRecord: URLRecord) {
        synchronized(mutex){
            logger.info ("created queue with host: $host")
            val newQueue = FrontierQueue(host, mutableListOf(urlRecord))
            queues.add(newQueue)
            mutex.notifyAll()
        }
    }

    override fun pullURLRecord(host: String): URLRecord? {
        synchronized(mutex){
            val queue = getQueue(host)
            val urlRecord = queue?.urlRecords?.removeFirstOrNull()

            if(urlRecord == null){
                deleteQueue(host)
            }

            return urlRecord
        }
    }

    private fun getQueue(host: String): FrontierQueue?{
        return queues.firstOrNull { it.host == host }
    }

    private fun deleteQueue(host: String){
        logger.info("removed queue with host: $host")
        queues.removeIf{it.host == host}
    }

    override fun pickFreeQueue(): String?{
        return synchronized(mutex) {
            queues.find { !it.isBlocked }?.let { queue ->
                queue.isBlocked = true
                queue.host
            }
        }
    }

    internal fun clear(){
        queues.clear()
    }
}