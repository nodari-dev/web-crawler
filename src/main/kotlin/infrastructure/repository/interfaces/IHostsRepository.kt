package infrastructure.repository.interfaces

import core.dto.URLInfo

interface IHostsRepository {
    fun update(host: String, urlsInfo: List<URLInfo>)
    fun get(host: String): MutableList<String>
}