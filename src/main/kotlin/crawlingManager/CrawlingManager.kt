package crawlingManager

import configuration.Configuration.SAVE_FILE_LOCATION
import dataExtractor.DataExtractor
import dto.HashedUrlPair
import storage.frontier.Frontier
import interfaces.ICrawlingManager
import mu.KotlinLogging
import parser.urlparser.URLParser
import redis.RedisConnector

object CrawlingManager: ICrawlingManager {
    private val frontier = Frontier
    private val jedis = RedisConnector.getJedis()
    private val urlParser = URLParser()
    private val dataExtractor = DataExtractor()
    private val logger = KotlinLogging.logger("CrawlingManager")

    /**
     * Sends starting points to frontier
     * Flushes all data from redis
     * @param seeds List of starting points
     */
    override fun startCrawling(seeds: List<String>){
        jedis.flushAll()
        if(seeds.isNotEmpty()){
            seeds.forEach { seed ->
                val host = urlParser.getHostWithProtocol(seed)
                frontier.updateOrCreateQueue(host, HashedUrlPair(seed))
            }
        } else{
            logger.error("No seed urls provided")
        }
    }

    /**
     * Calls data extractor to save txt file
     * @param html to generate seo data from
     * @param url to specific page
     */
    override fun extractSEOData(html: String, url: String) {
        dataExtractor.extractSEODataToFile(html, url, SAVE_FILE_LOCATION)
    }
}