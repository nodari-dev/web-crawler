package modules.interfaces

import core.dto.URLData

interface IQueuesManager {
    fun provideSeeds(seeds: List<URLData>)
    fun requestCallToCrawlersManager(host: String)
}