package application.interfaces

interface IHostsStorage {
    fun provideHost(host: String)
    fun deleteHost(host: String)
    fun isURLAllowed(host: String, url: String): Boolean
}