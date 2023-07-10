package frontier

import dto.FrontierQueue
import dto.URLRecord
import interfaces.IFrontier
import localStorage.URLHashStorage
import mu.KotlinLogging

object Frontier: IFrontier {
    private val queues = mutableListOf<FrontierQueue>()
    private val mutex = Object()
    private val logger = KotlinLogging.logger("Frontier")

    override fun pullURLRecord(host: String): URLRecord? {
        return synchronized(mutex){
            val queue = queues.firstOrNull { it.host == host }
            val urlRecord = queue?.urlRecords?.removeFirstOrNull()

            if(urlRecord == null){
                deleteQueue(host)
            }
            urlRecord
        }
    }

    private fun deleteQueue(host: String){
        queues.removeIf{it.host == host}
    }

    override fun updateOrCreateQueue(host: String, url: String) {
        synchronized(mutex){
            val urlRecord = URLRecord(url)
            println("added, $urlRecord")
            if(isQueueDefined(host)){
                updateQueue(host, urlRecord)
            } else{
                createQueue(host, urlRecord)
            }
            mutex.notifyAll()
        }
    }

    private fun isQueueDefined(host: String): Boolean{
        return synchronized(mutex){
            queues.any { queue -> queue.host == host }
        }
    }

    private fun updateQueue(host: String, urlRecord: URLRecord) {
        synchronized(mutex){
            queues.forEach { queue ->
                if(queue.host == host){
                    queue.urlRecords.add(urlRecord)
                }
            }
        }
    }

    private fun createQueue(host: String, urlRecord: URLRecord) {
        logger.info ("created queue with host: $host")

        synchronized(mutex){
            queues.add(FrontierQueue(host, mutableListOf(urlRecord)))
            mutex.notifyAll()
        }
    }

    override fun getQueue(host: String): FrontierQueue?{
        return synchronized(mutex){
            queues.firstOrNull { it.host == host }
        }
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