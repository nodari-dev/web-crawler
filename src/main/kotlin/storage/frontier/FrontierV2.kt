package storage.frontier

import application.interfaces.memoryGateways.IMemoryRepository
import mu.KotlinLogging
import storage.frontier.Configuration.DEFAULT_PATH
import core.dto.URLData
import application.interfaces.ISubscriber
import storage.interfaces.IFrontierV2

class FrontierV2(
    private val gateway: IMemoryRepository
): IFrontierV2 {
    private var logger = KotlinLogging.logger("Frontier")
    private val subscribers = mutableListOf<ISubscriber>()

    fun testMe(){
        subscribers.forEach{subscriber -> subscriber.sendMessage()}
    }

    override fun updateOrCreateQueue(host: String, url: String) {
        if(isQueueDefinedForHost(host)){
            updateQueue(host, url)
        } else{
            createQueue(host, url)
        }
    }

    private fun isQueueDefinedForHost(host: String): Boolean{
        return gateway.isEntryKeyDefined(DEFAULT_PATH, host)
    }

    private fun updateQueue(host: String, url: String) {
        gateway.updateEntry(DEFAULT_PATH, host, url)
    }

    private fun createQueue(host: String, url: String) {
        logger.info ("created queue with host: $host")
        gateway.createEntry(DEFAULT_PATH, host)

        gateway.updateEntry(DEFAULT_PATH, host, url)
    }

    override fun pullWebLink(host: String): URLData? {
        return URLData(gateway.getFirstEntryItem(DEFAULT_PATH, host))
    }

    override fun isQueueEmpty(host: String): Boolean{
        return gateway.checkEntryEmptiness(DEFAULT_PATH, host)
    }

    override fun deleteQueue(host: String) {
        logger.info("removed queue with host: $host")
        gateway.deleteEntry(DEFAULT_PATH, host)
    }
}