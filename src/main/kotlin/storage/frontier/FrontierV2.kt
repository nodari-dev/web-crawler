package storage.frontier

import application.interfaces.memoryGateways.IFrontierRepository
import mu.KotlinLogging
import core.dto.URLData
import storage.interfaces.IFrontierV2

class FrontierV2(
    private val frontierRepository: IFrontierRepository
): IFrontierV2 {
    private var logger = KotlinLogging.logger("Frontier")

    override fun updateOrCreateQueue(host: String, urls: List<String>) {
        if(isQueueDefinedForHost(host)){
            updateQueue(host, urls)
        } else{
            createQueue(host, urls)
        }
    }

    private fun isQueueDefinedForHost(host: String): Boolean{
        val queues = frontierRepository.getQueues()
        return queues?.contains(host) ?: false
    }

    private fun createQueue(host: String, urls: List<String>) {
        frontierRepository.create(host, urls)
        logger.info ("created queue with host: $host")
    }

    private fun updateQueue(host: String, urls: List<String>) {
        frontierRepository.update(host, urls)
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
}