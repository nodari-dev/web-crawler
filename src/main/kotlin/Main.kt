
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

    val test = SingleHostCrawl(Page("https://www.ecospace.org.ua"))

    test.start()

}