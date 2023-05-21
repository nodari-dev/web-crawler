
class Frontier {
    private val urls = mutableListOf<String>("host0.com", "host1.com", "host2.com",)
    private val mutex = Object()

    fun add(value: String) {
        synchronized(mutex){
            urls.add(value)
            mutex.notifyAll()
        }
    }

    private fun removeString(): String? {
        return urls.removeFirstOrNull()
    }

    fun getString(): String? {
        synchronized(mutex) {
            while (urls.isEmpty()) {
                mutex.wait()
            }
            val value = removeString()
    
            if(value != null){
                return value
            }
            return null
        }
    }
}

class Crawler(private val id: Int, private val frontier: Frontier): Thread(){
    override fun run() {
        while (true) {
            val url = frontier.getString()
            if(url != null){

                println("C: $id got string: $url")
                sleep(1000)

                // imitation sending new urls to frontier
                val modifiedString = "$url/abc"
                frontier.add(modifiedString)
            }
        }
    }
}

fun main() {
    val frontier = Frontier()
    val threads = mutableListOf<Thread>()

    for(i in 0..1){
        val crawler = Crawler(i, frontier)
        threads.add(crawler)
        crawler.start()
    }

    threads.forEach { thread ->
        thread.join()
    }

    println("All threads finished")

}