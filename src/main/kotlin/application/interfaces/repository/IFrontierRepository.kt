package application.interfaces.repository

interface IFrontierRepository {
    fun clear()
    fun get(host: String): String?
    fun update(host: String, list: List<String>)
    fun assignCrawler(id: Int, host: String)
    fun unassignCrawler(id: Int, host: String)
    fun getAvailableQueue(): String?
}