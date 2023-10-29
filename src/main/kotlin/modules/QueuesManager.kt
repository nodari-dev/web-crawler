package modules

import modules.interfaces.ICrawlersManagerV2
import modules.interfaces.IQueuesManager
import storage.interfaces.IFrontierV2

class QueuesManager(
    private val crawlersManagerV2: ICrawlersManagerV2,
    private val frontier: IFrontierV2
): IQueuesManager {
    override fun startMonitoring() {
        // TODO: PERIODIC POLLING AND CHECKING FRONTIER
    }


}