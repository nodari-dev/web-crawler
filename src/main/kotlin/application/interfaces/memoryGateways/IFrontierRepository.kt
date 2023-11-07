package application.interfaces.memoryGateways


interface IFrontierRepository {
    fun create(host: String, list: List<String>)
    fun update(host: String, list: List<String>)
    fun assignCrawler(host: String, crawlerId: String)
    fun unassignCrawler(host: String, crawlerId: String)
    fun getQueuesInfo(): MutableMap<String, String>?
    fun isQueueDefined(host: String): Boolean
    fun getLastItem(host: String): String?
    fun delete(host: String)
    fun clear()
}