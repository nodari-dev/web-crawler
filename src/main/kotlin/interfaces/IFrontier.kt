package interfaces

import dto.FrontierQueue

interface IFrontier {

    fun pullURL(host: String): String?
    fun updateOrCreateQueue(host: String, urls: MutableList<String>)
    fun getQueue(host: String): FrontierQueue?
    fun pickFreeQueue(): String?
}