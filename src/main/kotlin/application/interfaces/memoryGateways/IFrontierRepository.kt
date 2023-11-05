package application.interfaces.memoryGateways


interface IFrontierRepository {
    fun clear()
    fun create(host: String, list: List<String>)
    fun update(host: String, list: List<String>)
    fun getQueues(): String
    fun getLastItem(host: String): String?
    fun delete(host: String)
}