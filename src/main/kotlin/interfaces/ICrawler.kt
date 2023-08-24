package interfaces

interface ICrawler {
    val primaryHost: String
    fun start()
}