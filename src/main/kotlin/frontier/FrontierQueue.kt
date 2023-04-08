package frontier

import java.util.*

open class FrontierQueue(urls: Queue<String>) {
    private val urls = urls

    fun getUrls(): Queue<String> {
        return urls
    }

    fun add(url: String){
        urls.add(url)
    }

    fun remove(): String{
        return urls.poll()
    }
}