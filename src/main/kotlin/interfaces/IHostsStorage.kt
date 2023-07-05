package interfaces

import dto.Host

interface IHostsStorage {
    fun add(hostURL: String, bannedURLs: List<String>)
    fun get(url: String): Host?
}