package frontier

import dto.CrawlerModes
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
    private val mutex = Object()
    private val logger = KotlinLogging.logger("Frontier")

    override fun addURL(value: String) {
        synchronized(mutex){
            if(!urls.contains(value)){
                urls.add(value)
                logger.info ("Got $value")
            }
            mutex.notifyAll()
        }
    }

    override fun getURL(): String? {
        synchronized(mutex) {
            while (urls.isEmpty()) {
                mutex.wait()
            }
            return urls.removeFirstOrNull()
        }
    }
}