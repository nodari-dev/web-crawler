package frontier

import dto.FrontierQueue
import dto.URLRecord
import interfaces.IFrontier
import mu.KotlinLogging

object Frontier: IFrontier {
    // 2. Each back-queue must have NAME AS A HOST NAME
    // 3. Each back-queue contains ONLY URLS with the same host
    // 4. If url has unrecognisible host -> create new queue with NEW HOST NAME
    // 5. Frontier works only with NEW URLS
    // 6. is urls was seen -> it will be refetched later (schedule)

    // after fetch -> put all found urls to frontier
    // frontier will handle all urls
    // save frontqs and backqs to text file (or update existing)

    // Number of BackQueues = NUMBER_OF_CRAWLERS -> create connection by host

    private val queues = mutableListOf<FrontierQueue>()
    private val mutex = Object()
    private val logger = KotlinLogging.logger("Frontier")

    override fun pullURLRecord(host: String): URLRecord? {
        return synchronized(mutex){
            val result = queues.firstOrNull { it.host == host }?.urlRecords?.removeFirstOrNull()
            println(result)
            result
        }
    }

    override fun updateOrCreateQueue(host: String, url: String) {
        synchronized(mutex){
            val urlRecord = URLRecord(url)
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