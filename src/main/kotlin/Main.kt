
class Frontier {
    private val strings = mutableListOf<String>("host0.com", "host1.com", "host2.com",)
    private val mutex = Object()

    fun add(string: String) {
        // add new url to frontier
        synchronized(mutex){
            strings.add(string)
            mutex.notifyAll()
        }
    }

    private fun removeString(): String? {
        // add remove url from frontier and return
        synchronized(mutex){
            if (strings.isEmpty()) {
                return null
            }
            return strings.removeFirst()
        }
    }

    //    fun getString(host: String): String? {
    fun getString(): String? {
        synchronized(mutex) {
            while (strings.isEmpty()) {
                mutex.wait()
            }
            val value = removeString()
            // Might be used if crawler will use specific host
//            if(value != null && value.contains(host)){
//                return value
//            }
            if(value != null){
                return value
            }
            return null
        }
    }
}

class Crawler(private val id: Int, private val frontier: Frontier): Thread(){
    //    private val host = "host$id.com"
    override fun run() {
        while (true) {
//            val string = frontier.getString(host) ?: break
            val string = frontier.getString() ?: break

            println("C: $id got string: $string")
            sleep(1000)

            // imitation sending new urls to frontier
            val modifiedString = "$string/abc"
            frontier.add(modifiedString)
        }
    }
}

fun main() {
//    val frontier = Frontier()
//    val threads = mutableListOf<Thread>()
//
//    for(i in 0..1){
//        val crawler = Crawler(i, frontier)
//        threads.add(crawler)
//        crawler.start()
//    }
//
//    threads.forEach { thread ->
//        thread.join()
//    }
//
//    println("All threads finished")

    val tree = BTS()
    val root = tree.add(20)
    tree.add(3)
    tree.add(3)
    tree.add(3)

    tree.show()

    val list = mutableListOf<Int>()

    tree.inorder(root, list)

    list.forEach{
        item -> println(item)
    }
}