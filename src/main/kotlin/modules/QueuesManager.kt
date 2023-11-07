package modules

import application.interfaces.IURLPacker
import core.dto.URLData
import modules.interfaces.ICrawlersManagerV2
import modules.interfaces.IQueuesManager
import storage.interfaces.IFrontierV2

class QueuesManager(
    private val crawlersManagerV2: ICrawlersManagerV2,
    private val frontierV2: IFrontierV2,
    private val urlPacker: IURLPacker
): IQueuesManager {

    override fun provideSeeds(seeds: List<URLData>){
        val packedURLs = urlPacker.pack(seeds)
        packedURLs.forEach{pack ->
            frontierV2.updateOrCreateQueue(pack.key, pack.value)
        }
    }

    override fun requestCallToCrawlersManager(host: String){
        val crawlerId = crawlersManagerV2.requestCrawlerInitializationAndGetId(host)
        frontierV2.assignCrawler(host, "$crawlerId")
    }
}