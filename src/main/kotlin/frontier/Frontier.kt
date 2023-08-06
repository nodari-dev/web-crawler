package frontier

import controller.Controller
import dto.FormattedURL
import dto.FrontierQueue
import dto.URLRecord
import interfaces.IFrontier
import mu.KotlinLogging
import java.util.concurrent.locks.ReentrantLock

object Frontier: IFrontier {
    private val queues = mutableListOf<FrontierQueue>()
    private val mutex = ReentrantLock()
    private val logger = KotlinLogging.logger("Frontier")
    private val controller = Controller

    override fun updateOrCreateQueue(host: String, formattedURL: FormattedURL) {
        mutex.lock()
        try{
            val urlRecord = URLRecord(formattedURL)
            when(isQueueDefined(host)){
                true -> updateQueue(host, urlRecord)
                else -> createQueue(host, urlRecord)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun isQueueDefined(host: String): Boolean{
        return queues.any { queue -> queue.host == host }
    }

    private fun updateQueue(host: String, urlRecord: URLRecord) {
        val queue = getQueue(host)
        queue?.urlRecords?.add(urlRecord)
    }

    private fun createQueue(host: String, urlRecord: URLRecord) {
        logger.info ("created queue with host: $host")

        val newQueue = FrontierQueue(host, mutableListOf(urlRecord))
        queues.add(newQueue)
        controller.addHost(newQueue.host)
    }

    override fun pullURLRecord(host: String): URLRecord? {
        mutex.lock()
        try{
            val queue = getQueue(host)
            val urlRecord = queue?.urlRecords?.removeFirstOrNull()

            if(urlRecord == null){
                deleteQueue(host)
            }

            return urlRecord
        } finally {
            mutex.unlock()
        }
    }

    private fun getQueue(host: String): FrontierQueue?{
        return queues.firstOrNull { it.host == host }
    }

    private fun deleteQueue(host: String){
        logger.info("removed queue with host: $host")
        queues.removeIf{it.host == host}
    }

    internal fun clear(){
        queues.clear()
    }
}