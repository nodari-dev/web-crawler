import crawlersManager.CrawlersManager
import frontierManager.FrontierManager


fun main() {
    val crawlerManager = CrawlersManager
    val frontierManager = FrontierManager
    frontierManager.addSeed("https://ecospace.org.ua")

    // TODO: KEYBOARD INTERRUPT -> JOIN THREADS

//    val pool = JedisPool("localhost", 6379)
//
//    pool.resource.use { jedis ->
//        jedis["foo"] = "bar"
//        println(jedis["foo"]) // prints bar
//    }

}
