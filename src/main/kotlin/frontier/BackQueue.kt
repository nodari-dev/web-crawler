package frontier

import java.util.*

class BackQueue (host: String, queue: Queue<String>): FrontierQueue(queue){
    private val host = host

    fun getHost(): String{
        return host
    }
}