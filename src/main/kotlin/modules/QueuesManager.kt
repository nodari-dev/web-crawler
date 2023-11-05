package modules

import modules.interfaces.ICrawlersManagerV2
import modules.interfaces.IQueuesManager
import storage.interfaces.IFrontierV2

class QueuesManager(
    private val crawlersManagerV2: ICrawlersManagerV2,
    private val frontierV2: IFrontierV2
): IQueuesManager {

    fun provideSeeds(seeds: List<String>){
//        frontierV2.updateOrCreateQueue()
    }
}