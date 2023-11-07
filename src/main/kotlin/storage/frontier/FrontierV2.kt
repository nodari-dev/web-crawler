package storage.frontier

import application.interfaces.memoryGateways.IFrontierRepository
import core.dto.URLData
import modules.interfaces.IQueuesManager
import mu.KLogger
import storage.interfaces.IFrontierV2

class FrontierV2(
    private val frontierRepository: IFrontierRepository,
    private val queuesManager: IQueuesManager,
    private val logger: KLogger,
): IFrontierV2 {

    override fun updateOrCreateQueue(host: String, urls: List<String>) {
        if(frontierRepository.isQueueDefined(host)){
            frontierRepository.update(host, urls)
        } else{
            queuesManager.requestCallToCrawlersManager(host)
            frontierRepository.create(host, urls)
            logger.info ("created queue with host: $host")
        }
    }

    override fun pullWebLink(host: String): URLData? {
        val url = frontierRepository.getLastItem(host)
        return if(url == null){
            null
        } else{
            URLData(url)
        }
    }

    override fun deleteQueue(host: String) {
        frontierRepository.delete(host)
        logger.info("removed queue with host: $host")
    }

    override fun assignCrawler(host: String, crawlerId: String) {
        frontierRepository.assignCrawler(host, crawlerId)
        logger.info("assigned crawler $crawlerId to queue with host: $host")
    }

    override fun unassignCrawler(host: String, crawlerId: String) {
        frontierRepository.unassignCrawler(host, crawlerId)
        logger.info("unassigned crawler $crawlerId from queue with host: $host")
    }

}