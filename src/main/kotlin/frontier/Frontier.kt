package frontier

import communicationManager.CommunicationManager
import dto.FormattedURL
import dto.FrontierQueue
import dto.FrontierRecord
import interfaces.IFrontier
import mu.KotlinLogging
import java.util.concurrent.locks.ReentrantLock

object Frontier: IFrontier {
    private val queues = mutableListOf<FrontierQueue>()
    private val mutex = ReentrantLock()
    private val logger = KotlinLogging.logger("Frontier")
    private val controller = CommunicationManager

    override fun updateOrCreateQueue(host: String, formattedURL: FormattedURL) {
        mutex.lock()
        try{
            val frontierRecord = FrontierRecord(formattedURL)
            when(isQueueDefined(host)){
                true -> updateQueue(host, frontierRecord)
                else -> createQueue(host, frontierRecord)
            }
        } finally {
            mutex.unlock()
        }
    }

    private fun isQueueDefined(host: String): Boolean{
        return queues.any { queue -> queue.host == host }
    }

    private fun updateQueue(host: String, frontierRecord: FrontierRecord) {
        val queue = getQueue(host)
        queue?.frontierRecords?.add(frontierRecord)
    }

    private fun createQueue(host: String, frontierRecord: FrontierRecord) {
        logger.info ("created queue with host: $host")

        val newQueue = FrontierQueue(host, mutableListOf(frontierRecord))
        queues.add(newQueue)
        controller.addHost(host)
    }

    override fun pullURLRecord(host: String): FrontierRecord? {
        mutex.lock()
        try{
            val queue = getQueue(host)
            val urlRecord = queue?.frontierRecords?.removeFirstOrNull()

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