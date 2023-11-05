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
//            updateQueue(host, urls)
        } else{
            createQueue(host, urls)
        }
    }

    private fun isQueueDefinedForHost(host: String): Boolean{
//        return repository.isEntryKeyDefined(DEFAULT_PATH, host)
        TODO("Not yet implemented")
    }

    private fun createQueue(host: String, urls: List<String>) {
        logger.info ("created queue with host: $host")
//        frontierRepository.createQueue(host, urls)
    }

    private fun updateQueue(host: String, urls: List<String>) {
//        repository.updateEntry(DEFAULT_PATH, host, url)
    }

    override fun pullWebLink(host: String): URLData? {
        TODO("Not yet implemented")
//        return URLData(repository.getFirstEntryItem(DEFAULT_PATH, host))
    }

    override fun isQueueEmpty(host: String): Boolean{
        TODO("Not yet implemented")
//        return repository.checkEntryEmptiness(DEFAULT_PATH, host)
    }

    override fun deleteQueue(host: String) {
        logger.info("removed queue with host: $host")
//        repository.deleteEntry(DEFAULT_PATH, host)
    }
}