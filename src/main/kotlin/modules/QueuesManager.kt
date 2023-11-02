package modules

import application.interfaces.ISubscriber
import modules.interfaces.ICrawlersManagerV2
import modules.interfaces.IQueuesManager

class QueuesManager(
    private val crawlersManagerV2: ICrawlersManagerV2,
): IQueuesManager, ISubscriber {
    override fun startMonitoring() {
        // TODO: PERIODIC POLLING AND CHECKING FRONTIER
    }

    override fun sendMessage() {
        println("Notified")
    }


}