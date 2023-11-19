package infrastructure.repository.interfaces

interface IHostsRepository {
    fun update(host: String, urls: List<String>)
    fun get(host: String): MutableList<String>
}