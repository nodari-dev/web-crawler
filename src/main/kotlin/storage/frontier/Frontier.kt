package storage.frontier

import mu.KLogger
import mu.KotlinLogging
import infrastructure.memoryGateways.RedisMemoryGateway
import modules.CrawlersManager
import storage.frontier.Configuration.DEFAULT_PATH
import storage.interfaces.IFrontier
import core.dto.WebLink

object Frontier: IFrontier {
    private var jedis = RedisMemoryGateway
    private var logger = KotlinLogging.logger("Frontier")
    private var crawlersManager = CrawlersManager

    override fun updateOrCreateQueue(host: String, url: String) {
        if(isQueueDefinedForHost(host)){
            updateQueue(host, url)
        } else{
            createQueue(host, url)
        }
    }

    private fun isQueueDefinedForHost(host: String): Boolean{
        return jedis.isEntryKeyDefined(DEFAULT_PATH, host)
    }

    private fun updateQueue(host: String, url: String) {
        jedis.updateEntry(DEFAULT_PATH, host, url)
    }

    private fun createQueue(host: String, url: String) {
        logger.info ("created queue with host: $host")
        jedis.createEntry(DEFAULT_PATH, host)

        jedis.updateEntry(DEFAULT_PATH, host, url)
        crawlersManager.requestCrawlerInitialization(host)
    }

    override fun pullURL(host: String): WebLink {
        return WebLink(jedis.getFirstEntryItem(DEFAULT_PATH, host))
    }

    override fun isQueueEmpty(host: String): Boolean{
        return jedis.checkEntryEmptiness(DEFAULT_PATH, host)
    }

    override fun deleteQueue(host: String){
        logger.info("removed queue with host: $host")
        jedis.deleteEntry(DEFAULT_PATH ,host)
    }

    fun setupTest(jedisMock: RedisMemoryGateway, loggerMock: KLogger, crawlersManagerMock: CrawlersManager){
        jedis = jedisMock
        logger = loggerMock
        crawlersManager = crawlersManagerMock
    }
}