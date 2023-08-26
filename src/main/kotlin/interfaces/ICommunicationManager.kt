package interfaces

import dto.HashedUrlPair

interface ICommunicationManager {
    fun startCrawling(seeds: List<String>)
    fun requestCrawlerInitialization(host: String)
    fun requestCrawlerTermination(crawler: Thread)
    fun requestURLFromFrontier(host: String): HashedUrlPair
    fun isFrontierQueueEmpty(host: String): Boolean
    fun sendURLToFrontierQueue(host: String, hashedUrlPair: HashedUrlPair)
}