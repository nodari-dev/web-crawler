package modules

import modules.interfaces.ICrawlersManagerV2
import modules.interfaces.IQueuesManager
import storage.interfaces.IFrontier

class QueuesManager(
    private val crawlersManagerV2: ICrawlersManagerV2,
    private val frontier: IFrontier
): IQueuesManager {
    override fun startMonitoring() {
        TODO("Not yet implemented")
    }
}