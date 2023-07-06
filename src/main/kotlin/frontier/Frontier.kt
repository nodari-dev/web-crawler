package frontier

import dto.CrawlerModes
import dto.FrontierQueue
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

    private val urls = mutableListOf<String>()
    private val queues = mutableListOf<FrontierQueue>()
    private val mutex = Object()
    private val logger = KotlinLogging.logger("Frontier")

    override fun pullURL(host: String): String? {
        return synchronized(mutex){
            queues.firstOrNull { it.host == host }?.urls?.removeFirstOrNull()
        }
    }

    override fun updateOrCreateQueue(host: String, urls: MutableList<String>) {
        synchronized(mutex){
            if(isQueueDefined(host)){
                updateExistingQueue(host, urls)
            } else{
                createQueue(host, urls)
            }
            mutex.notifyAll()
        }
    }

    private fun isQueueDefined(host: String): Boolean{
        return synchronized(mutex){
            queues.any { queue -> queue.host == host }
        }
    }

    private fun updateExistingQueue(host: String, urls: MutableList<String>) {
        synchronized(mutex){
            queues.forEach { queue ->
                if(queue.host == host){
                    queue.urls.addAll(urls)
                    queue.isBlocked = true
                }
            }
        }
    }

    private fun createQueue(host: String, urls: MutableList<String>) {
        logger.info ("created queue with host: $host")

        synchronized(mutex){
            queues.add(FrontierQueue(host, urls))
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