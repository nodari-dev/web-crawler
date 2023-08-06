import crawlersManager.CrawlersManager
import frontierManager.FrontierManager


fun main() {
    val frontierManager = FrontierManager
    frontierManager.addSeed("https://ecospace.org.ua")
    frontierManager.start()

//    val pool = JedisPool("localhost", 6379)
//
//    pool.resource.use { jedis ->
//        jedis["foo"] = "bar"
//        println(jedis["foo"]) // prints bar
//    }

}
