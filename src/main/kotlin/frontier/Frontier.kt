package frontier

import dto.WebURL
import services.DBConnector

class Frontier {
    // 1. Each queue must have MAX number of urls
    // 2. Each back-queue must have NAME AS A HOST NAME
    // 3. Each back-queue contains ONLY URLS with the same host
    // 4. If url has unrecognisible host -> create new queue with NEW HOST NAME
    // 5. Frontier works only with NEW URLS
    // 6. is urls was seen -> it will be refetched later (schedule)

    // after fetch -> put all found urls to frontier
    // frontier will handle all urls
    // frontier works on separated thread
    //
    //

    // IMPORTANT
    // scheduler -> shared resource which contains all backq

    private val queuesUtils = QueuesUtils()
    private val connection = DBConnector().init()
    private val scheduler = Scheduler()
    private val mutex = Object()

    fun getUrl(): String{
        synchronized(mutex){
            return ""
        }
    }

    fun addUrls(urls: List<WebURL>) {
        println("Added ${urls.size} urls")
    }


//        queuesUtils.executeFrontQMutation(connection, 12, urls)
//
//        val test: Queue<Int> = PriorityQueue<Int>()
//        test.offer(10)
//        test.offer(1)
//        test.offer(5)
//
//        while (test.isNotEmpty()){
//            println(test.poll())
//        }

//        val frontq = queuesUtils.getFrontQueues(connection)
//        if(frontq != null){
//            while (frontq.next()){
//                val id = frontq.getInt("id")
//                val name = frontq.getString("name")
//
//                println("$id, $name")
//            }
//        }

//    }

//    init {
//        val q1: Queue<String> = LinkedList<String>(mutableListOf("1", "2", "3"))
//        val q2: Queue<String> = LinkedList<String>(mutableListOf())
//
//        val front: MutableList<Queue<String>> = mutableListOf(q1)
//        val back: MutableList<Queue<String>> = mutableListOf(q2)
//
//        front.forEach{queue ->
//            while (queue.isNotEmpty()){
//                println("Front: $front")
//                println("Back: $back")
//                back[0].add(queue.poll())
//                println("Front: $front")
//                println("Back: $back")
//            }
//        }
//    }
}