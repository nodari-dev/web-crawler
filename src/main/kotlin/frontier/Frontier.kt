package frontier

import dto.CrawlerModes
import interfaces.IFrontier

class Frontier: IFrontier {
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

    override fun addURL(value: String) {
        synchronized(mutex){
            urls.add(value)
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