open class Crawler: Thread() {
    private var busy = false

    fun isBusy(): Boolean{
        return busy
    }

    fun disable(){
        busy = true
    }

    fun enable(){
        busy = false
    }

    override fun run(){}

    open fun update(){}

    open fun setSubject(sub: Subject?){}

    fun addUrlToFrontier(url: String){
        Frontier.addNewUrl(url)
    }
}

interface Subject {
    //methods to register and unregister crawler
    fun register(obj: Crawler?)
    fun unregister(obj: Crawler?)

    //method to notify crawlers of change
    fun notifyCrawlers()

    //method to get updates from subject
    fun getUpdate(obj: Crawler?): Any?
}

object Frontier : Subject {
    private val observers: MutableList<Crawler?>

    private val urls: MutableList<String> = mutableListOf("url-0-")
    private var queues: MutableList<String> = mutableListOf()

    private var changed = false
    private val mutex = Object()

    init {
        observers = ArrayList()
    }

    fun addNewUrl(url: String){
        urls.add(url)
        urls.forEach{item -> println("Frontier has new url $item")}
        createQueue("-NEW Q with $url-")
    }

    override fun register(obj: Crawler?) {
        if (obj == null) throw NullPointerException("Null Observer")
        synchronized(mutex) {
            if (!observers.contains(obj)) {
                observers.add(obj)
            }
        }
    }

    override fun unregister(obj: Crawler?) {
        synchronized(mutex) {
            observers.remove(obj)
        }
    }

    override fun notifyCrawlers() {
        var observersLocal: List<Crawler?>? = null
        //synchronization is used to make sure any observer registered after message is received is not notified
        synchronized(mutex) {
            if (!changed) return
            observersLocal = ArrayList(observers)
            changed = false
        }
        for (obj in observersLocal!!) {
            obj!!.update()
        }
    }

    override fun getUpdate(obj: Crawler?): Any? {
        return queues.removeFirstOrNull()
    }

    //method to post message to the topic
    fun createQueue(queue: String) {
        println("Created a queue: $queue")
        queues.add(queue)
        changed = true
        notifyCrawlers()
    }
}

class FrontierSubscriber(private val name: String) : Crawler() {
    private var topic: Subject? = null
    override fun update() {
        if(!isBusy()){
            val msg = topic!!.getUpdate(this) as String?
            if (msg == null) {
                println("$name noting to work on")
                disable()
            } else println("$name started working on $msg")
        }
    }

    override fun setSubject(sub: Subject?) {
        topic = sub
    }
}

fun main() {
    val frontier = Frontier
    for(i in 1..3){
        val crawler: Crawler = FrontierSubscriber("crawler${i}")
        frontier.register(crawler)
        crawler.setSubject(frontier)
        crawler.addUrlToFrontier("url-$i")
        crawler.update()
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