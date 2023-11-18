package modules.interfaces

import core.dto.URLInfo

interface IQueuesManager {
    fun provideSeeds(seeds: List<URLInfo>)
    fun requestCallToCrawlersManager(host: String)
}