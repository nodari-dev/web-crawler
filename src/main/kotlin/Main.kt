import crawler.Configuration
import crawler.CrawlersController
import org.omg.CORBA.Object
import kotlin.math.sign

class Signal<T> {

    class Connection{
        private var open: Boolean = true

        fun isOpened():Boolean{
            return open
        }

        fun disable(){
            open = false
        }

        fun enable(){
            open = true
        }
    }

    lateinit var connection: Connection

    val callbacks = mutableMapOf<Connection, (T) -> Unit>()

    fun emit(newValue: T) {
        for(cb in callbacks) {
            if(cb.key.isOpened()){
                cb.value(newValue)
            }

        }
    }

    fun connect(callback: (newValue: T) -> Unit) : Connection {
        connection = Connection()
        callbacks[connection] = callback
        return connection
    }

    fun disconnect(connection : Connection) {
        callbacks.remove(connection)
    }
}

object Frontier: Thread() {
    val backQInserted = Signal<String>()
    private val storage = mutableListOf<String>()

    private val mutex = Object()

    fun onUrlAdded(url: String){
        synchronized(mutex){
            storage.add(url)
            if(url in storage){
                println("Frontier: new url was added: $url")
            }
        }
    }

    fun createBackQ(){
        synchronized(mutex){
            if(storage.isNotEmpty()){
                println("Frontier: new backQ with ${storage.first()}")
                backQInserted.emit(storage.removeFirst())
            }

        }
    }
    override fun run(){
        while (true){
            createBackQ()
        }
    }
}

class Crawler(private val id: Int): Thread() {
    val backQStorage = mutableListOf<String>()
    val urlAdded = Signal<String>()

    fun onBackQInserted(newBackQ : String) {
        if(backQStorage.isEmpty()){
            println("Crawler is working with: $newBackQ")
            backQStorage.add(newBackQ)
        }
    }

    fun sendUrlToFrontier(url: String){
        println("Crawler sent url $url")
        urlAdded.emit(url)
    }

    override fun run(){
        sendUrlToFrontier("www.host.com/${currentThread().id}")
        println("Started Crawler $id on thread ${currentThread().id}")
    }
}

fun main() {
    val frontier = Frontier
    frontier.start()

    for (id: Int in 1..2) {
        val crawler = Crawler(id)
        frontier.backQInserted.connect(crawler::onBackQInserted)
        crawler.urlAdded.connect(frontier::onUrlAdded)
        crawler.start()
    }

//    val seeds: List<String> = listOf(
//        "https://ecospace.org.ua",
//        "https://magecloud.agency"
//    )
//
//    val controller = CrawlersController()
//    controller.addSeeds(seeds)
//    controller.startCrawling()
}